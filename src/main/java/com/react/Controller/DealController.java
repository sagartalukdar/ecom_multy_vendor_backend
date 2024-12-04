package com.react.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.react.Model.Deal;
import com.react.Response.ApiResponse;
import com.react.Service.DealService;

@RestController
@RequestMapping("/api/deals")
public class DealController {

	@Autowired
	private DealService dealService;
	
	@PostMapping("/create")
	public ResponseEntity<Deal> createDeal(@RequestBody Deal deal){
		return new ResponseEntity<Deal>(dealService.createDeal(deal),HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Deal>> getDeals() throws Exception{
		return new ResponseEntity<List<Deal>>(dealService.getDeals(),HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Deal> updateDeal(@PathVariable Long id,@RequestBody Deal deal) throws Exception{
		return new ResponseEntity<Deal>(dealService.updateDeal(deal, id),HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteDeal(@PathVariable Long id) throws Exception{
		dealService.deleteDeal(id);
		ApiResponse res=new ApiResponse("deal deleted");
		return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
	}
	
	
}
