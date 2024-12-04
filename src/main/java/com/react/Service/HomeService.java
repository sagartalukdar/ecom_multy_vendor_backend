package com.react.Service;

import java.util.List;

import com.react.Model.Home;
import com.react.Model.HomeCategory;

public interface HomeService {

	public Home createHomePageData(List<HomeCategory> categories);
}
