package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.JabatanModel;

public interface JabatanService {
	//View
	Optional<JabatanModel> getJabatanById(Long id);
	List<JabatanModel> getAllJabatan();
	
	//Add
	void addJabatan(JabatanModel jabatanBaru);
	
	//Update
	void updateJabatan(Long id, JabatanModel jabatanBaru);
	
	//Delete
	void deleteById(Long id);
}
