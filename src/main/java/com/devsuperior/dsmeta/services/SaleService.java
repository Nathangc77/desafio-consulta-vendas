package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SummaryMinDTO;
import com.devsuperior.dsmeta.projections.SaleReportProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

	@Transactional(readOnly = true)
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleReportDTO> searchReport(String minDate, String maxDate, String name, Pageable pageable) {
		LocalDate today = maxDate.equalsIgnoreCase("") ?
				LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()): LocalDate.parse(maxDate);
		LocalDate pastDate = minDate.equalsIgnoreCase("") ?
				today.minusYears(1L): LocalDate.parse(minDate);
		Page<SaleReportDTO> result = repository.searchReport(pastDate, today, name, pageable);
		return result;
	}

//	IMPLEMENTAÇÃO USANDO SQL RAIZ
//	public Page<SaleReportDTO> searchReport(String minDate, String maxDate, String name, Pageable pageable) {
//		LocalDate today = maxDate.equalsIgnoreCase("") ?
//				LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()): LocalDate.parse(maxDate);
//		LocalDate pastDate = minDate.equalsIgnoreCase("") ?
//				today.minusYears(1L): LocalDate.parse(minDate);
//		Page<SaleReportProjection> result = repository.searchReport(pastDate, today, name, pageable);
//        return result.map(x -> new SaleReportDTO(x));
//	}
	public List<SummaryMinDTO> searchSummary(String minDate, String maxDate) {
		LocalDate today = maxDate.equalsIgnoreCase("") ?
				LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()): LocalDate.parse(maxDate);
		LocalDate pastDate = minDate.equalsIgnoreCase("") ?
				today.minusYears(1L): LocalDate.parse(minDate);
		return repository.searchSummary(pastDate, today);
	}
}
