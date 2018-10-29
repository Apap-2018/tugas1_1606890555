package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.ProvinsiModel;

public interface ProvinsiService {
	//get
	Optional<ProvinsiModel> getProvinsiById(Long id);
	ProvinsiModel getProvinsiByNama(String nama);
	List<ProvinsiModel> getAllProvinsi();
	
	//add
	void addProvinsi(ProvinsiModel provinsi);
}
