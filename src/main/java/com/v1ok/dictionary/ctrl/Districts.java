package com.v1ok.dictionary.ctrl;

import com.v1ok.commons.IRestResponse;
import com.v1ok.commons.impl.RestResponse;
import com.v1ok.dictionary.db.model.DistrictsEntity;
import com.v1ok.dictionary.db.service.IDistrictsService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("districts")
public class Districts {

  final IDistrictsService districtsService;

  public Districts(IDistrictsService districtsService) {
    this.districtsService = districtsService;
  }

  @GetMapping
  public IRestResponse<List<DistrictsEntity>> findBy(String parentId) {

    List<DistrictsEntity> districtsEntities = this.districtsService.findBy(parentId);

    return RestResponse.builder(districtsEntities);

  }

}
