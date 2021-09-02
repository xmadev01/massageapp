package com.xms.app.massage.service;

import com.xms.app.massage.model.Template;
import com.xms.app.massage.paging.*;
import com.xms.app.massage.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Override
    public List<Template> getAllTemplates() {
        return templateRepository.findAll().stream().filter(template -> template.isActive()).collect(Collectors.toList());
    }

    @Override
    public Page<Template> getPage(PagingRequest pagingRequest) {

        List<Template> templates = new ArrayList<>();

        //Sorting ...
        Order order = pagingRequest.getOrder().get(0);
        int columnIndex = order.getColumn();
        Column column = pagingRequest.getColumns().get(columnIndex);
        if (pagingRequest.getOrder().get(0).getDir() == Direction.asc) {
            templates = templateRepository.findAll(Sort.by(Sort.Direction.ASC, column.getData()));
        } else if (pagingRequest.getOrder().get(0).getDir() == Direction.desc) {
            templates = templateRepository.findAll(Sort.by(Sort.Direction.DESC, column.getData()));
        }
        final List<Template> filteredPractitioner = templates.stream()
                .filter(template -> template.isActive())
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .collect(Collectors.toList());
        long count = templates.stream().filter(template -> template.isActive())
                .count();
        Page<Template> page = new Page<>(filteredPractitioner);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());
        return page;
    }

    @Override
    public Template loadTemplate(final long templateId) {
        Optional<Template> template = templateRepository.findById(templateId);
        return template.isPresent() ? template.get() : null;
    }

    @Override
    public Optional<Template> findById(long id) {
        return Optional.empty();
    }

    @Override
    public void saveTemplate(Template template) {
        templateRepository.save(template);
    }

    @Override
    public void deactivateTemplate(final long templateId) {
        Optional<Template> template = templateRepository.findById(templateId);
        if (template.isPresent()) {
            template.get().setActive(!template.get().isActive());
        }
    }
}
