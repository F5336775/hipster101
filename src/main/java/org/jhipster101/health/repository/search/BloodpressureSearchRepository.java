package org.jhipster101.health.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.List;
import org.jhipster101.health.domain.Bloodpressure;
import org.jhipster101.health.repository.BloodpressureRepository;
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
 * Spring Data Elasticsearch repository for the {@link Bloodpressure} entity.
 */
public interface BloodpressureSearchRepository
    extends ElasticsearchRepository<Bloodpressure, Long>, BloodpressureSearchRepositoryInternal {}

interface BloodpressureSearchRepositoryInternal {
    Page<Bloodpressure> search(String query, Pageable pageable);

    Page<Bloodpressure> search(Query query);

    @Async
    void index(Bloodpressure entity);

    @Async
    void deleteFromIndexById(Long id);
}

class BloodpressureSearchRepositoryInternalImpl implements BloodpressureSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final BloodpressureRepository repository;

    BloodpressureSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, BloodpressureRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Bloodpressure> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Bloodpressure> search(Query query) {
        SearchHits<Bloodpressure> searchHits = elasticsearchTemplate.search(query, Bloodpressure.class);
        List<Bloodpressure> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Bloodpressure entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Bloodpressure.class);
    }
}
