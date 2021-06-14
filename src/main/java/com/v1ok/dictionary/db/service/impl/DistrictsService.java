package com.v1ok.dictionary.db.service.impl;

import com.google.common.collect.Maps;
import com.v1ok.db.service.AbstractService;
import com.v1ok.dictionary.db.model.DistrictsEntity;
import com.v1ok.dictionary.db.model.DistrictsEntity.DistrictsEntityBuilder;
import com.v1ok.dictionary.db.service.IDistrictsService;
import com.v1ok.dictionary.db.service.impl.support.AMAPData;
import com.v1ok.dictionary.db.service.impl.support.Districts;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DistrictsService extends AbstractService<DistrictsEntity, String> implements
    IDistrictsService {


  final RestTemplate client;
  final String AMAP_URL = "https://restapi.amap.com/v3/config/district?keywords=中国&subdistrict=4&key=86ab61aaa308175a4a282d6ae39a38b9";

  @Autowired
  public DistrictsService(RestTemplate client) {
    this.client = client;
  }

  @Override
  public List<DistrictsEntity> findBy(String parentId) {

    if (StringUtils.isBlank(parentId)) {
      parentId = "1c3de80a18ec2b3272034fcb10bd435a";
    }

    return this.dao.getQuery().where().eq("parentId", parentId).orderBy("adCode").findList();
  }

  @Override
  public Map<Character, List<DistrictsEntity>> letterIndex(String parentId) {

    List<DistrictsEntity> entities = this.findBy(parentId);

    if (CollectionUtils.isEmpty(entities)) {
      return new HashMap<>();
    }

    Map<Character, List<DistrictsEntity>> index = new TreeMap<>();

    entities.forEach(districtsEntity -> {
      String name = districtsEntity.getName();
      Character letter = convertTo(name);
      List<DistrictsEntity> districtsEntities = index
          .computeIfAbsent(letter, k -> new LinkedList<>());
      districtsEntities.add(districtsEntity);
    });

    return index;
  }

  @Override
  public Map<String, List<DistrictsEntity>> provinceIndex() {
    List<DistrictsEntity> entities = this.findBy(null);
    if (CollectionUtils.isEmpty(entities)) {
      return Maps.newHashMap();
    }

    Map<String, List<DistrictsEntity>> value = new HashMap<>();

    entities.forEach(districtsEntity -> {
      String pid = districtsEntity.getPid();
      value.put(pid, this.findBy(pid));
    });

    return value;
  }

  private Character convertTo(String name) {
    String[] x = PinyinHelper.toHanyuPinyinStringArray(CharUtils.toChar(name));
    String s = Arrays.stream(x).findFirst().orElse(null);
    assert s != null;
    return StringUtils.upperCase(s).charAt(0);
  }


  public void initData() {

    int count = this.dao.getQuery().findCount();

    if (count > 0) {
      return;
    }

    log.info("初始化【Districts】表数据");

    AMAPData data = this.client.getForObject(AMAP_URL, AMAPData.class);

    if (data != null && "1".equals(data.getStatus())) {
      this.save(data.getDistricts(), (String) null);
    }

    log.info("初始化【Districts】成功");
  }

  private void save(Districts[] districts, String parentId) {

    if (ArrayUtils.isEmpty(districts)) {
      return;
    }

    SimpleTypeConverter converter = new SimpleTypeConverter();

    for (Districts d : districts) {

      Object citycode = d.getCitycode();
      String adcode = d.getAdcode();
      String name = d.getName();
      String center = d.getCenter();
      String level = d.getLevel();

      DistrictsEntityBuilder districtsEntityBuilder = DistrictsEntity.builder();
      if (citycode instanceof String) {
        districtsEntityBuilder.cityCode((String) citycode);
      }
      String[] split = StringUtils.split(center, ',');

      String pid = DigestUtils.md5Hex(adcode + name);
      Double longitude = converter.convertIfNecessary(split[0], Double.class);
      Double latitude = converter.convertIfNecessary(split[1], Double.class);

      DistrictsEntity entity = districtsEntityBuilder.adCode(adcode)
          .name(name)
          .level(level)
          .longitude(longitude)
          .latitude(latitude)
          .pid(pid)
          .parentId(parentId).build();

      this.save(entity);

      Districts[] subDis = d.getDistricts();
      if (ArrayUtils.isNotEmpty(subDis)) {
        this.save(subDis, pid);
      }

    }

  }

}
