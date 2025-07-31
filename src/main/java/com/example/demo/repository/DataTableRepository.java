package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.DataTableEntity;

@Repository
public interface DataTableRepository extends JpaRepository<DataTableEntity, Long> {
        @Query(value = "SELECT * FROM data_table WHERE " +
                        "custom_columns->>'customField_1' LIKE %:search% OR " +
                        "custom_columns->>'customField_2' LIKE %:search%", nativeQuery = true)
        List<DataTableEntity> searchInCustomColumns(@Param("search") String search);

        @Query(value = "SELECT * FROM data_table " +
                        "WHERE to_tsvector('simple', custom_columns::text) @@ plainto_tsquery(:search)", nativeQuery = true)
        List<DataTableEntity> fullTextSearchInJson(@Param("search") String search);

        @Query(value = "SELECT * FROM data_table " +
                        "WHERE (:search IS NULL OR :search = '' OR " +
                        "to_tsvector('simple', custom_columns::text) @@ plainto_tsquery(:search))", nativeQuery = true)
        List<DataTableEntity> fullTextSearchInJson2(@Param("search") String search);

        // pgroonga를 사용한 다국어 검색 쿼리 + 직접 페이징
        @Query(value = "SELECT * FROM data_table " +
                        "WHERE custom_columns::text &@~ :search " +
                        "ORDER BY id DESC " +
                        "LIMIT :limit OFFSET :offset", nativeQuery = true)
        List<DataTableEntity> searchCustomColumnsValue(
                        @Param("search") String search,
                        @Param("limit") int limit,
                        @Param("offset") int offset);

        @Query(value = "SELECT * FROM data_table " +
                        "WHERE custom_columns::text ILIKE CONCAT('%', :search, '%') " +
                        "ORDER BY id DESC " +
                        "LIMIT :limit OFFSET :offset", nativeQuery = true)
        List<DataTableEntity> searchCustomColumnsValueV2(
                        @Param("search") String search,
                        @Param("limit") int limit,
                        @Param("offset") int offset);

        @Query(value = "SELECT * FROM data_table " +
                        "ORDER BY id DESC " +
                        "LIMIT :limit OFFSET :offset", nativeQuery = true)
        List<DataTableEntity> searchAllColumnsValue(
                        @Param("limit") int limit,
                        @Param("offset") int offset);

        /**
         * 전체 조회 쿼리 최적화 V2
         * 
         * @param limit
         * @param offset
         * @return
         */
        @Query(value = "SELECT * FROM data_table " +
                        "WHERE id <= (" +
                        "  SELECT id FROM data_table " +
                        "  ORDER BY id DESC " +
                        "  OFFSET :offset LIMIT 1" +
                        ") " +
                        "ORDER BY id DESC " +
                        "LIMIT :limit", nativeQuery = true)
        List<DataTableEntity> searchAllColumnsValueV2(
                        @Param("limit") int limit,
                        @Param("offset") int offset);

        /**
         * 전체 조회 쿼리 최적화 V3
         * [마지막 ID 조회] + 오프셋 조회 2단계 쿼리
         * 
         * @param offset
         * @return
         */
        @Query(value = "SELECT id FROM data_table " +
                        "  ORDER BY id DESC " +
                        "  OFFSET :offset LIMIT 1", nativeQuery = true)
        Long searchDataTableLastId(@Param("offset") int offset);

        @Query(value = "SELECT * FROM data_table " +
                        "WHERE id <= :lastId " +
                        "ORDER BY id DESC " +
                        "LIMIT :limit", nativeQuery = true)
        /**
         * 전체 조회 쿼리 최적화 V3
         * 마지막 ID 조회 + [오프셋 조회] 2단계 쿼리
         * 
         * @param offset
         * @return
         */
        List<DataTableEntity> searchAllColumnsValueV3(
                        @Param("limit") int limit,
                        @Param("lastId") Long lastId);

        // pgroonga를 사용한 다국어 검색 쿼리 + Pageable
        @Query(value = "SELECT * FROM data_table " +
                        "WHERE custom_columns::text &@~ :search", nativeQuery = true)
        Page<DataTableEntity> searchWithPgroongaPageable(@Param("search") String search, Pageable pageable);
}
