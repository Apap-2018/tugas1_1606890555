package com.apap.tugas1.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;

@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private InstansiService instansiService;
	
	@Autowired
	private JabatanService jabatanService;

	@Autowired
	private ProvinsiService provinsiService;
	
	
	//Landing page
	@RequestMapping("/")
	public String index(Model model) {
		List<JabatanModel> listJabatan = jabatanService.getAllJabatan();
		List<InstansiModel> listInstansi = instansiService.getAllInstansi();
		
		model.addAttribute("listJabatan", listJabatan);
		model.addAttribute("listInstansi", listInstansi);
		model.addAttribute("header", "Home");
		return "home";
	}
	
	//Menampilkan data pegawai berdasarkan NIP (fitur 1)
	@RequestMapping("/pegawai")
	public String viewPegawai(@RequestParam String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiByNip(nip);
		model.addAttribute("pegawai", pegawai);
		
		//instansi dan jabatan
		List<JabatanModel> jabatanPegawai = pegawai.getJabatan();
		model.addAttribute("jabatanPegawai", jabatanPegawai);
		
		InstansiModel instansi = pegawai.getInstansi();
		String provinsiPegawai = instansi.getProvinsi().getNama();
		String instansiPegawai = instansi.getNama() + " - " + provinsiPegawai;
		model.addAttribute("instansiPegawai", instansiPegawai);
		
		//perhitungan gaji
		Double totalGaji = 0.0;
		for (JabatanModel jabatan : jabatanPegawai) {
			Double gajiPokok = jabatan.getGajiPokok();
			Double persenGaji = instansi.getProvinsi().getPresentaseTunjangan();
			Double hasilHitungGaji = (gajiPokok + (persenGaji * gajiPokok / 100));
			if (hasilHitungGaji > totalGaji) { totalGaji = hasilHitungGaji; }
		}
		model.addAttribute("gajiPegawai", totalGaji);
		
		model.addAttribute("header", "Data Pegawai");
		return "viewPegawai";
	}
	
	//Menampilkan data pegawai tertua dan termuda di setiap instansi (fitur 10)
	@RequestMapping("/termuda-tertua")
	public String viewPegawaiTermudaTertua(@RequestParam Long idInstansi, Model model) {
		List<PegawaiModel> listPegawai = pegawaiService.getTuaMudaInstansi
				(instansiService.getInstansiById(idInstansi).get());
		
		model.addAttribute("pegawaiTertua", listPegawai.get(0));
		model.addAttribute("pegawaiTermuda", listPegawai.get(listPegawai.size()-1));
		return "termudaTertua";
	}
	
	//Menambah pegawai (fitur 2)
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	public String addPegawai (Model model) {
		List<InstansiModel> listInstansi = instansiService.getAllInstansi();
		model.addAttribute("listInstansi", listInstansi);
		
		List<JabatanModel> listJabatan = jabatanService.getAllJabatan();
		model.addAttribute("listJabatan", listJabatan);
		
		List<ProvinsiModel> listProvinsi = provinsiService.getAllProvinsi();
		model.addAttribute("listProvinsi", listProvinsi);
		
		PegawaiModel pegawai = new PegawaiModel();
		pegawai.setJabatan(new ArrayList<JabatanModel>());
		pegawai.getJabatan().add(new JabatanModel());
		model.addAttribute("pegawai", pegawai);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		model.addAttribute("tanggalLahir", dateFormat.format(date));
		
		model.addAttribute("header", "Tambah Pegawai");
		return "addPegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST, params={"addJabatan"})
	private String addRow(@ModelAttribute PegawaiModel pegawai, Model model) {
		model.addAttribute("header", "Tambah Pegawai");
		pegawai.getJabatan().add(new JabatanModel());
		model.addAttribute("pegawai", pegawai);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String tanggalLahir = simpleDateFormat.format(pegawai.getTanggalLahir());
		model.addAttribute("tanggalLahir", tanggalLahir);
		
		List<InstansiModel> listInstansi = instansiService.getInstansiFromProvinsi(pegawai.getInstansi().getProvinsi());
		model.addAttribute("listInstansi", listInstansi);
		
		List<JabatanModel> listJabatan = jabatanService.getAllJabatan();
		model.addAttribute("listJabatan", listJabatan);
		
		List<ProvinsiModel> listProvinsi = provinsiService.getAllProvinsi();
		model.addAttribute("listProvinsi", listProvinsi);
		
		return "addPegawai";
	}
	
	@RequestMapping(value = "/instansi/getFromProvinsi", method = RequestMethod.GET)
	@ResponseBody
	public List<InstansiModel> getInstansi(@RequestParam (value = "id", required = true) Long id) {
	    ProvinsiModel provinsi = provinsiService.getProvinsiById(id).get();
		return instansiService.getInstansiFromProvinsi(provinsi);
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(
	            dateFormat, false));
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST, params={"deleteJabatan"})
	private String deleteRow(@ModelAttribute PegawaiModel pegawai, Model model, HttpServletRequest req) {
		model.addAttribute("header", "Tambah Pegawai");
		Integer rowId =  Integer.valueOf(req.getParameter("deleteJabatan"));
		pegawai.getJabatan().remove(rowId.intValue());
		model.addAttribute("pegawai", pegawai); 
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String tanggalLahir = simpleDateFormat.format(pegawai.getTanggalLahir());
		model.addAttribute("tanggalLahir", tanggalLahir);
		
		List<InstansiModel> listInstansi = instansiService.getInstansiFromProvinsi(pegawai.getInstansi().getProvinsi());
		model.addAttribute("listInstansi", listInstansi);
		
		List<JabatanModel> listJabatan = jabatanService.getAllJabatan();
		model.addAttribute("listJabatan", listJabatan);
		
		List<ProvinsiModel> listProvinsi = provinsiService.getAllProvinsi();
		model.addAttribute("listProvinsi", listProvinsi);
		return "addPegawai";
	}
	
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	public String addPegawai (@ModelAttribute PegawaiModel pegawai, Model model) {
		pegawaiService.addPegawai(pegawai);
		
		model.addAttribute("pegawai", pegawai);
		String message = "Pegawai dengan NIP " + pegawai.getNip() + " berhasil ditambah";
		model.addAttribute("header", message);
		return "addPegawaiSuccess";
	}
	
	//Megupdate pegawai (fitur 2)
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.GET)
	public String updatePegawai (@RequestParam String nip, Model model) {
		List<InstansiModel> listInstansi = instansiService.getAllInstansi();
		model.addAttribute("listInstansi", listInstansi);
		
		List<JabatanModel> listJabatan = jabatanService.getAllJabatan();
		model.addAttribute("listJabatan", listJabatan);
		
		List<ProvinsiModel> listProvinsi = provinsiService.getAllProvinsi();
		model.addAttribute("listProvinsi", listProvinsi);
		
		PegawaiModel pegawai = pegawaiService.getPegawaiByNip(nip);
		model.addAttribute("pegawai", pegawai);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		model.addAttribute("tanggalLahir", dateFormat.format(date));
		
		model.addAttribute("header", "Mengubah Pegawai");
		return "updatePegawai";
	}
	
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.POST, params={"addJabatan"})
	private String addRowUbah(@ModelAttribute PegawaiModel pegawai, Model model) {
		model.addAttribute("header", "Mengubah Pegawai");
		pegawai.getJabatan().add(new JabatanModel());
		model.addAttribute("pegawai", pegawai);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String tanggalLahir = simpleDateFormat.format(pegawai.getTanggalLahir());
		model.addAttribute("tanggalLahir", tanggalLahir);
		
		List<InstansiModel> listInstansi = instansiService.getInstansiFromProvinsi(pegawai.getInstansi().getProvinsi());
		model.addAttribute("listInstansi", listInstansi);
		
		List<JabatanModel> listJabatan = jabatanService.getAllJabatan();
		model.addAttribute("listJabatan", listJabatan);
		
		List<ProvinsiModel> listProvinsi = provinsiService.getAllProvinsi();
		model.addAttribute("listProvinsi", listProvinsi);
		
		return "updatePegawai";
	}
	
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.POST, params={"deleteJabatan"})
	private String deleteRowUbah(@ModelAttribute PegawaiModel pegawai, Model model, HttpServletRequest req) {
		model.addAttribute("header", "Mengubah Pegawai");
		Integer rowId =  Integer.valueOf(req.getParameter("deleteJabatan"));
		pegawai.getJabatan().remove(rowId.intValue());
		model.addAttribute("pegawai", pegawai); 
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String tanggalLahir = simpleDateFormat.format(pegawai.getTanggalLahir());
		model.addAttribute("tanggalLahir", tanggalLahir);
		
		List<InstansiModel> listInstansi = instansiService.getInstansiFromProvinsi(pegawai.getInstansi().getProvinsi());
		model.addAttribute("listInstansi", listInstansi);
		
		List<JabatanModel> listJabatan = jabatanService.getAllJabatan();
		model.addAttribute("listJabatan", listJabatan);
		
		List<ProvinsiModel> listProvinsi = provinsiService.getAllProvinsi();
		model.addAttribute("listProvinsi", listProvinsi);
		return "updatePegawai";
	}
	
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.POST)
	public String updatePegawai (@ModelAttribute PegawaiModel pegawaiBaru, Model model) {
		try {
			String nip = pegawaiBaru.getNip();
			pegawaiService.update(nip, pegawaiBaru);
			
			model.addAttribute("pegawai", pegawaiBaru);
			String message = "Pegawai dengan NIP " + nip + " berhasil diubah";
			model.addAttribute("header", message);
			return "addPegawaiSuccess";
		} catch(Exception e) {
			return "redirect:error";
		}
	}
	
	@RequestMapping("/pegawai/ubah/error")
	public String errorUpdate () {
		return "error/404";
	}
	
	//Pegawai Termuda dan Tertua
	@RequestMapping(value="/pegawai/termuda-tertua", method=RequestMethod.GET)
	private String viewPegawaiTermudaTertua(@RequestParam("id") long id, Model model) {
		InstansiModel instansi = instansiService.getInstansiById(id).get();
		List<PegawaiModel> listPegawai =  pegawaiService.getTuaMudaInstansi(instansi);
		
		PegawaiModel pegawai_muda = listPegawai.get(listPegawai.size()-1);
		PegawaiModel pegawai_tua = listPegawai.get(0);
		
		List<JabatanModel> jabatanPegawaiMuda = pegawai_muda.getJabatan();
		model.addAttribute("jabatanPegawaiMuda", jabatanPegawaiMuda);
		
		List<JabatanModel> jabatanPegawaiTua = pegawai_tua.getJabatan();
		model.addAttribute("jabatanPegawaiTua", jabatanPegawaiTua);
		
		model.addAttribute("pegawaiMuda", pegawai_muda);
		model.addAttribute("pegawaiTua", pegawai_tua);
		
		return "viewTermudaTertua";
	}
}
