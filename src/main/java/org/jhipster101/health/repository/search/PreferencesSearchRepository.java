package org.jhipster101.health.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.List;
import org.jhipster101.health.domain.Preferences;
import org.jhipster101.health.repository.PreferencesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Preferences} entity.
 */
public interface PreferencesSearchRepository extends ElasticsearchRepository<Preferences, Long>, PreferencesSearchRepositoryInternal {}

interface PreferencesSearchRepositoryInternal {
    Page<Preferences> search(String query, Pageable pageable);

    Page<Preferences> search(Query query);

    @Async
    void index(Preferences entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PreferencesSearchRepositoryInternalImpl implements PreferencesSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PreferencesRepository repository;

    PreferencesSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PreferencesRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Preferences> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Preferences> search(Query query) {
        SearchHits<Preferences> searchHits = elasticsearchTemplate.search(query, Preferences.class);
        List<Preferences> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Preferences entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Preferences.class);
    }
}
