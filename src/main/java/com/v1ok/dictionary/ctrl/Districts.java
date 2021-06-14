package com.v1ok.dictionary.ctrl;

import com.v1ok.commons.HeadCode;
import com.v1ok.commons.IRestResponse;
import com.v1ok.commons.impl.RestResponse;
import com.v1ok.dictionary.db.model.DistrictsEntity;
import com.v1ok.dictionary.db.service.IDistrictsService;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("districts")
public class Districts {

  final IDistrictsService districtsService;

  public Districts(IDistrictsService districtsService) {
    this.districtsService = districtsService;
  }

  @GetMapping()
  public IRestResponse<?> findBy(
      @RequestParam(value = "parentId", required = false) String parentId) {

    List<DistrictsEntity> districtsEntities = this.districtsService.findBy(parentId);

    if (CollectionUtils.isNotEmpty(districtsEntities)) {
      return RestResponse.builder(districtsEntities);
    }

    return RestResponse.builder().error(HeadCode.NOT_FIND);
  }

  @GetMapping("index")
  public IRestResponse<?> letterIndex(
      @RequestParam(value = "parentId", required = false) String parentId) {
    Map<Character, List<DistrictsEntity>> index1 = districtsService.letterIndex(parentId);
    return RestResponse.builder(index1);
  }

  @GetMapping("province-index")
  public IRestResponse<?> provinceIndex(){
    Map<String, List<DistrictsEntity>> provinceIndex = districtsService.provinceIndex();
    return RestResponse.builder(provinceIndex);
  }

}
