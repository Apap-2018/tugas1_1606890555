package com.apap.tugas1.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.repository.PegawaiDb;

@Service
@Transactional
public class PegawaiServiceImpl implements PegawaiService {
	@Autowired
	private PegawaiDb pegawaiDb;
	
	@Override
	public PegawaiModel getPegawaiByNip(String nip) {
		return pegawaiDb.findByNip(nip).get(0);
	}

	@Override
	public List<PegawaiModel> getPegawaiByInstansi(InstansiModel instansi) {
		return pegawaiDb.findByInstansi(instansi);
	}

	@Override
	public List<PegawaiModel> getPegawaiByJabatan(JabatanModel jabatan) {
		return pegawaiDb.findByJabatan(jabatan);
	}

	@Override
	public List<PegawaiModel> getPegawaiByProvinsi(Long provinsiId) {
		List<PegawaiModel> pegawaiByProvinsi = new ArrayList<PegawaiModel>();
		for (PegawaiModel pegawai : pegawaiDb.findAll()) {
			if (pegawai.getInstansi().getProvinsi().getId() == provinsiId) {
				pegawaiByProvinsi.add(pegawai);
			}
		}
		return pegawaiByProvinsi;
	}
	
	@Override
	public List<PegawaiModel> getTuaMudaInstansi(InstansiModel instansi){
		return pegawaiDb.findByInstansiOrderByTanggalLahirAsc(instansi);
	}

	@Override
	public void addPegawai(PegawaiModel pegawai) {
		InstansiModel instansi = pegawai.getInstansi();
		String kodeInstansi = Long.toString(instansi.getId());
		
		Date tanggalLahir = pegawai.getTanggalLahir();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
		String tglLahir = simpleDateFormat.format(tanggalLahir).replaceAll("-", "");
		
		String tahunMasuk = pegawai.getTahunMasuk();
		
		int urutanPegawai;
		List<PegawaiModel> listPegawai = pegawaiDb.findByInstansiAndTanggalLahirAndTahunMasuk
				(instansi, tanggalLahir, tahunMasuk);
		if (listPegawai.isEmpty()) {
			urutanPegawai = 1;
		}
		else {
			urutanPegawai = (int)(Long.parseLong(listPegawai.get(listPegawai.size()-1).getNip())%100) + 1;
		}
		
		String nip = kodeInstansi + tglLahir + tahunMasuk + urutanPegawai;
		pegawai.setNip(nip);
		pegawaiDb.save(pegawai);
	}

	@Override
	public void update(String nip, PegawaiModel pegawai) {
		InstansiModel instansi = pegawai.getInstansi();
		String kodeInstansi = Long.toString(instansi.getId());
		
		Date tanggalLahir = pegawai.getTanggalLahir();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
		String tglLahir = simpleDateFormat.format(tanggalLahir).replaceAll("-", "");
		
		String tahunMasuk = pegawai.getTahunMasuk();
		
		int urutanPegawai;
		List<PegawaiModel> listPegawai = pegawaiDb.findByInstansiAndTanggalLahirAndTahunMasuk
				(instansi, tanggalLahir, tahunMasuk);
		if (listPegawai.isEmpty()) {
			urutanPegawai = 1;
		}
		else {
			urutanPegawai = (int)(Long.parseLong(listPegawai.get(listPegawai.size()-1).getNip())%100) + 1;
		}
		
		String nipBaru = kodeInstansi + tglLahir + tahunMasuk + urutanPegawai;
		pegawai.setNip(nipBaru);
		pegawaiDb.save(pegawai);
	}
	
	

}
	
