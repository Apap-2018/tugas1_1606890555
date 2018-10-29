package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.repository.ProvinsiDb;

@Service
@Transactional
public class ProvinsiServiceImpl implements ProvinsiService{
	@Autowired
	ProvinsiDb provinsiDb;
	
	@Override
	public Optional<ProvinsiModel> getProvinsiById(Long id) {
		return provinsiDb.findById(id);
	}

	@Override
	public List<ProvinsiModel> getAllProvinsi() {
		return provinsiDb.findAll();
	}


	@Override
	public void addProvinsi(ProvinsiModel provinsi) {
		provinsiDb.save(provinsi);
	}

	@Override
	public ProvinsiModel getProvinsiByNama(String nama) {
		return provinsiDb.findByNama(nama).get(0);
	}
}
