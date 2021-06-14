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

}
