package com.v1ok.dictionary.ctrl;

import com.v1ok.commons.IRestResponse;
import com.v1ok.commons.impl.RestResponse;
import com.v1ok.dictionary.db.model.IndustryEntity;
import com.v1ok.dictionary.db.service.IIndustryService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("industry")
public class Industry {

  final IIndustryService industryService;

  public Industry(IIndustryService industryService) {
    this.industryService = industryService;
  }

  @GetMapping("{parentCode}")
  public IRestResponse<List<IndustryEntity>> list(@PathVariable("parentCode") String parentCode) {
    List<IndustryEntity> all = industryService.findAll("parentCode", parentCode);
    return RestResponse.builder(all);
  }

  @GetMapping
  public IRestResponse<IndustryEntity> findOne(@RequestParam("code") String code) {
    Optional<IndustryEntity> optional = this.industryService.findOne(code);
    return RestResponse.builder(optional.orElse(null));
  }

}
