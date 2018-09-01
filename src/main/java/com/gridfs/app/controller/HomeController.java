package com.gridfs.app.controller;

import java.io.IOException;
import java.util.StringJoiner;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

@Controller
public class HomeController {

	@Autowired
	GridFsTemplate gridFsTemplate;

	@RequestMapping(value = { "/", "/home" })
	public String getHome(Model model) {
		GridFsResource[] gridFsResource = gridFsTemplate.getResources("*");
		model.addAttribute("gs", gridFsResource);
		return "home";
	}

	@RequestMapping(value = "/upload")
	public String getUplaod() {
		return "upload";
	}

	@RequestMapping(value = "/delete/{id}")
	public String delete(@PathVariable String id) {
		gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));
		return "redirect:/home";
	}

	@RequestMapping(value = "/download/{id}")
	public void download(@PathVariable String id, HttpServletResponse response) {
		GridFSDBFile gridFsdbFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));

		byte[] b;
		try {
			b = IOUtils.toByteArray(gridFsdbFile.getInputStream());
			response.setContentType(gridFsdbFile.getContentType());
			response.getOutputStream().write(b);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@PostMapping("/search")
	public String search(@ModelAttribute Search search,Model model) {
		GridFsResource[] gridFsResource = gridFsTemplate.getResources(search.getName().toLowerCase().trim()+"*");
		model.addAttribute("gs", gridFsResource);
		return "home";
	}
	
	@PostMapping("/upload")
	public String multiFileUpload(@ModelAttribute UploadForm form, RedirectAttributes redirectAttributes) {

		StringJoiner sj = new StringJoiner(" , ");

		for (MultipartFile file : form.getFiles()) {

			if (file.isEmpty()) {
				continue; // next pls
			}

			try {
				DBObject metaData = new BasicDBObject();
				metaData.put("fileName", file.getName());
				String id = gridFsTemplate
						.store(file.getInputStream(), file.getOriginalFilename().toLowerCase(), file.getContentType(), metaData)
						.getId().toString();

				sj.add(file.getOriginalFilename());

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		String uploadedFileName = sj.toString();
		if (StringUtils.isEmpty(uploadedFileName)) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
		} else {
			redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + uploadedFileName + "'");
		}

		return "redirect:/uploadStatus";

	}

	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}
}
