package com.v1ok.dictionary.db.service;

import com.v1ok.db.service.IService;
import com.v1ok.dictionary.db.model.DistrictsEntity;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IDistrictsService extends IService<DistrictsEntity, String> {

  void initData() throws IOException;

  List<DistrictsEntity> findBy(String parentId);

  Map<Character, List<DistrictsEntity>> letterIndex(String parentId);

  Map<String,List<DistrictsEntity>> provinceIndex();

}
