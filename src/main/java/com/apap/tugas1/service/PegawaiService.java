package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;

public interface PegawaiService {
	//get
	PegawaiModel getPegawaiByNip(String nip);
	List<PegawaiModel> getPegawaiByInstansi(InstansiModel instansi);
	List<PegawaiModel> getPegawaiByJabatan(JabatanModel jabatan);
	List<PegawaiModel> getPegawaiByProvinsi(Long provinsiId);
	List<PegawaiModel> getTuaMudaInstansi(InstansiModel instansi);
	
	//add
	void addPegawai(PegawaiModel pegawai);
	
	//update
	void update(String nip, PegawaiModel pegawaiBaru);
}
