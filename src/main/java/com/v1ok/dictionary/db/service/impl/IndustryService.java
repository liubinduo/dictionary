package com.v1ok.dictionary.db.service.impl;

import com.v1ok.db.service.AbstractService;
import com.v1ok.dictionary.db.model.IndustryEntity;
import com.v1ok.dictionary.db.service.IIndustryService;
import io.ebean.annotation.Transactional;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class IndustryService extends AbstractService<IndustryEntity, String> implements
    IIndustryService {

}
