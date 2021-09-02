package com.xms.app.massage.service;

import com.xms.app.massage.model.Template;
import com.xms.app.massage.paging.Page;
import com.xms.app.massage.paging.PagingRequest;

import java.util.List;
import java.util.Optional;

public interface TemplateService {
    List<Template> getAllTemplates();

    Page<Template> getPage(PagingRequest pagingRequest);

    Template loadTemplate(long templateId);

    Optional<Template> findById(long id);
    void saveTemplate(final Template template);

    void deactivateTemplate(long templateId);
}
