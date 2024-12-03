package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SummaryMinDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(value = "SELECT new com.devsuperior.dsmeta.dto.SaleReportDTO(obj.id, obj.date, obj.amount, obj.seller.name) " +
            "FROM Sale obj " +
            "WHERE obj.date BETWEEN :pastDate AND :today " +
            "AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Page<SaleReportDTO> searchReport(LocalDate pastDate, LocalDate today, String name, Pageable pageable);

    //	IMPLEMENTAÇÃO USANDO SQL RAIZ
//    @Query(value = "SELECT sales.id, sales.date, sales.amount, tb_seller.name " +
//            "FROM tb_sales AS sales " +
//            "INNER JOIN tb_seller ON sales.seller_id = tb_seller.id " +
//            "WHERE sales.date BETWEEN :pastDate AND :today " +
//            "AND UPPER(tb_seller.name) LIKE UPPER(CONCAT('%', :name, '%'))",
//            countQuery = "SELECT COUNT(sales.id) " +
//                    "FROM tb_sales AS sales " +
//                    "INNER JOIN tb_seller ON sales.seller_id = tb_seller.id " +
//                    "WHERE sales.date BETWEEN :pastDate AND :today " +
//                    "AND UPPER(tb_seller.name) LIKE UPPER(CONCAT('%', :name, '%'))",
//            nativeQuery = true)
//    Page<SaleReportProjection> searchReport(LocalDate pastDate, LocalDate today, String name, Pageable pageable);

    @Query(value = "SELECT new com.devsuperior.dsmeta.dto.SummaryMinDTO(obj.seller.name, SUM(obj.amount)) " +
            "FROM Sale obj " +
            "WHERE obj.date BETWEEN :pastDate AND :today " +
            "GROUP BY obj.seller.name")
    List<SummaryMinDTO> searchSummary(LocalDate pastDate, LocalDate today);
}
