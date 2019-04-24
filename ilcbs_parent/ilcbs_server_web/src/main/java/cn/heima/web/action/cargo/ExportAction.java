package cn.heima.web.action.cargo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ModelDriven;

import cn.heima.domain.Contract;
import cn.heima.domain.Export;
import cn.heima.domain.ExportProduct;
import cn.heima.service.ContractService;
import cn.heima.service.ExportProductService;
import cn.heima.service.ExportService;
import cn.heima.utils.Page;
import cn.heima.utils.UtilFuns;
import cn.heima.web.action.BaseAction;

@Namespace("/cargo")
@Result(name="alist",type="redirectAction",location="exportAction_list")
public class ExportAction extends BaseAction implements ModelDriven<Export> {

	private Export model = new Export();
	@Override
	public Export getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	private Page page = new Page();
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}

	@Autowired
	private ContractService contractService;
	
	@Autowired
	private ExportService exportService;
	
	@Autowired
	private ExportProductService exportProductService;
	
	/**
	 * 合同管理内购销合同列表
	 * @return
	 * @throws Exception
	 */
	@Action(value="exportAction_contractList",results= {@Result(name="contractList",location="/WEB-INF/pages/cargo/export/jContractList.jsp")})
	public String contractList() throws Exception {
		
		Specification<Contract> spec = new Specification<Contract>() {

			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> arg1, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("state").as(Integer.class), 1);
			}
		};
		org.springframework.data.domain.Page<Contract> page2 = contractService.findPage(spec, new PageRequest(page.getPageNo()-1, page.getPageSize()));
		
		page.setResults(page2.getContent());
		page.setTotalPage(page2.getTotalPages());
		page.setTotalRecord(page2.getTotalElements());
		page.setUrl("exportAction_contractList");
		super.push(page);
		return "contractList";
	}
	
	@Action(value="exportAction_tocreate",results= {@Result(name="tocreate",location="/WEB-INF/pages/cargo/export/jExportCreate.jsp")})
	public String tocreate() throws Exception{
		
		
		return "tocreate";
	}
	
	@Action(value="exportAction_insert")
	public String insert() {
		
		exportService.saveOrUpdate(model);
		
		return "alist";
	}

	@Action(value="exportAction_list",results= {@Result(name="tolist",location="/WEB-INF/pages/cargo/export/jExportList.jsp")})
	public String list() {
		
		org.springframework.data.domain.Page<Export> page2 = exportService.findPage(null, new PageRequest(page.getPageNo()-1, page.getPageSize()));
		
		page.setResults(page2.getContent());
		page.setTotalPage(page2.getTotalPages());
		page.setTotalRecord(page2.getTotalElements());
		page.setUrl("exportAction_list");
		super.push(page);
		
		
		return "tolist";
	}
	
	
	@Action(value="exportAction_toview",results= {@Result(name="toview",location="/WEB-INF/pages/cargo/export/jExportView.jsp")})
	public String toview() throws Exception {
		
		Export export = exportService.get(model.getId());
		
		super.push(export);
		return "toview";
	}
	
	@Action(value="exportAction_delete")
	public String delete() throws Exception{
		
		String[] ids = model.getId().split(", ");
		
		exportService.delete(ids);
		return "alist";
	}
	
	/**
	 * 提交出口报运
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "exportAction_submit")
	public String submit() throws Exception {

		String[] ids = model.getId().split(", ");
		for (String id : ids) {

			Export export = exportService.get(id);
			export.setState(1);
			exportService.saveOrUpdate(export);
		}

		return "alist";
	}

	/**
	 * 取消出口报运
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "exportAction_cancel")
	public String cancel() throws Exception {

		String[] ids = model.getId().split(", ");
		for (String id : ids) {
			Export export = exportService.get(id);
			export.setState(0);
			exportService.saveOrUpdate(export);
		}

		return "alist";
	}
	
	@Action(value="exportAction_toupdate",results= {@Result(name="toupdate",location="/WEB-INF/pages/cargo/export/jExportUpdate.jsp")})
	public String toupdate() throws Exception {
	
		Export export = exportService.get(model.getId());
		super.push(export);
		
		return "toupdate";
	}
	
	@Action(value="exportAction_getTabledoData")
	public String getTabledoData() throws Exception {
//		addTRRecord('mRecordTable', 'id', 'productNo', 'cnumber', 'grossWeight', 'netWeight', 'sizeLength', 'sizeWidth', 'sizeHeight', 'exPrice', 'tax')
		System.out.println(model.getId());
		
		Specification<ExportProduct> spec = new Specification<ExportProduct>() {

			@Override
			public Predicate toPredicate(Root<ExportProduct> root, CriteriaQuery<?> arg1, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("export").get("id").as(String.class), model.getId());
			}
		};
		
		List<ExportProduct> exportProductList = exportProductService.find(spec);
		for (ExportProduct exportProduct : exportProductList) {
			System.out.println(exportProduct);
		}
		
		ArrayList list = new ArrayList<>();
		for (ExportProduct exportProduct : exportProductList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			map.put("id", exportProduct.getId());
			map.put("productNo",exportProduct.getProductNo());
			map.put("cnumber", UtilFuns.convertNull(exportProduct.getCnumber()));
			map.put("grossWeight", UtilFuns.convertNull(exportProduct.getGrossWeight()));
			map.put("netWeight", UtilFuns.convertNull(exportProduct.getNetWeight()));
			map.put("sizeLength", UtilFuns.convertNull(exportProduct.getSizeLength()));
			map.put("sizeWidth", UtilFuns.convertNull(exportProduct.getSizeWidth()));
			map.put("sizeHeight", UtilFuns.convertNull(exportProduct.getSizeHeight()));
			map.put("exPrice",UtilFuns.convertNull(exportProduct.getExPrice()));
			map.put("tax", UtilFuns.convertNull(exportProduct.getTax()));
			
			list.add(map);
		}
		for (Object object : list) {
			System.out.println(object);
		}
		
		
		String jsonString = JSON.toJSONString(list);
		
		System.out.println(jsonString);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonString);
		
		return NONE;
	}
	
	@Action(value="exportAction_update")
	public String update() throws Exception {
		//出口报运单的保存
		Export export = exportService.get(model.getId());
		export.setInputDate(model.getInputDate());
		export.setLcno(model.getLcno());
		export.setConsignee(model.getConsignee());
		export.setShipmentPort(model.getShipmentPort());
		export.setDestinationPort(model.getDestinationPort());
		export.setTransportMode(model.getTransportMode());
		export.setPriceCondition(model.getPriceCondition());
		export.setMarks(model.getMarks());
		export.setRemark(model.getRemark());
	
		exportService.saveOrUpdate(export);
		
		//出口报运修改的商品
		for(int i=0;i<mr_id.length;i++) {
			if(mr_changed[i].equals("1")) {
				ExportProduct exportProduct = exportProductService.get(mr_id[i]);
				exportProduct.setCnumber(mr_cnumber[i]);
				exportProduct.setGrossWeight(mr_grossWeight[i]);
				exportProduct.setNetWeight(mr_netWeight[i]);
				exportProduct.setSizeLength(mr_sizeLength[i]);
				exportProduct.setSizeWidth(mr_sizeWidth[i]);
				exportProduct.setSizeHeight(mr_sizeHeight[i]);
				exportProduct.setExPrice(mr_exPrice[i]);
				exportProduct.setTax(mr_tax[i]);
				exportProductService.saveOrUpdate(exportProduct);
			}
		}
		
		
		
		return "alist";
	}
	
	private String[] mr_id;
	private String[] mr_changed;
	private Integer[] mr_cnumber;
	private Double[] mr_grossWeight;
	private Double[] mr_netWeight;
	private Double[] mr_sizeLength;
	private Double[] mr_sizeWidth;
	private Double[] mr_sizeHeight;
	private Double[] mr_exPrice;
	private Double[] mr_tax;
	public String[] getMr_id() {
		return mr_id;
	}
	public void setMr_id(String[] mr_id) {
		this.mr_id = mr_id;
	}
	public String[] getMr_changed() {
		return mr_changed;
	}
	public void setMr_changed(String[] mr_changed) {
		this.mr_changed = mr_changed;
	}
	public Integer[] getMr_cnumber() {
		return mr_cnumber;
	}
	public void setMr_cnumber(Integer[] mr_cnumber) {
		this.mr_cnumber = mr_cnumber;
	}
	public Double[] getMr_grossWeight() {
		return mr_grossWeight;
	}
	public void setMr_grossWeight(Double[] mr_grossWeight) {
		this.mr_grossWeight = mr_grossWeight;
	}
	public Double[] getMr_netWeight() {
		return mr_netWeight;
	}
	public void setMr_netWeight(Double[] mr_netWeight) {
		this.mr_netWeight = mr_netWeight;
	}
	public Double[] getMr_sizeLength() {
		return mr_sizeLength;
	}
	public void setMr_sizeLength(Double[] mr_sizeLength) {
		this.mr_sizeLength = mr_sizeLength;
	}
	public Double[] getMr_sizeWidth() {
		return mr_sizeWidth;
	}
	public void setMr_sizeWidth(Double[] mr_sizeWidth) {
		this.mr_sizeWidth = mr_sizeWidth;
	}
	public Double[] getMr_sizeHeight() {
		return mr_sizeHeight;
	}
	public void setMr_sizeHeight(Double[] mr_sizeHeight) {
		this.mr_sizeHeight = mr_sizeHeight;
	}
	public Double[] getMr_exPrice() {
		return mr_exPrice;
	}
	public void setMr_exPrice(Double[] mr_exPrice) {
		this.mr_exPrice = mr_exPrice;
	}
	public Double[] getMr_tax() {
		return mr_tax;
	}
	public void setMr_tax(Double[] mr_tax) {
		this.mr_tax = mr_tax;
	}
	
	
}