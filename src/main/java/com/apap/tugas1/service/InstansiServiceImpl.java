package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.repository.InstansiDb;
import com.apap.tugas1.repository.PegawaiDb;

@Service
@Transactional
public class InstansiServiceImpl implements InstansiService{
	@Autowired
	InstansiDb instansiDb;
	
	@Autowired
	PegawaiDb pegawaiDb;
	
	@Override
	public Optional<InstansiModel> getInstansiById(Long id) {
		return instansiDb.findById(id);
	}

	@Override
	public List<InstansiModel> getAllInstansi() {
		return instansiDb.findAll();
	}

	@Override
	public List<InstansiModel> getInstansiFromProvinsi(ProvinsiModel provinsi) {
		return instansiDb.findByProvinsi(provinsi);
	}

	@Override
	public void addInstansi(InstansiModel instansi) {
		instansiDb.save(instansi);
	}
	
	@Override
	public List<PegawaiModel> getTuaMudaInstansi(InstansiModel instansi){
		return pegawaiDb.findByInstansiOrderByTanggalLahirAsc(instansi);
	}
}
