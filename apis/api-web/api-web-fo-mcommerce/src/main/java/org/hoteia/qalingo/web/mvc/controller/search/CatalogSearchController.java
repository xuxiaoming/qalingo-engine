/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.8.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2014
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.web.mvc.controller.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.hoteia.qalingo.core.Constants;
import org.hoteia.qalingo.core.ModelConstants;
import org.hoteia.qalingo.core.domain.Cart;
import org.hoteia.qalingo.core.domain.CatalogCategoryMaster_;
import org.hoteia.qalingo.core.domain.CatalogCategoryVirtual;
import org.hoteia.qalingo.core.domain.CatalogCategoryVirtualProductSkuRel_;
import org.hoteia.qalingo.core.domain.CatalogCategoryVirtual_;
import org.hoteia.qalingo.core.domain.MarketArea;
import org.hoteia.qalingo.core.domain.ProductMarketing;
import org.hoteia.qalingo.core.domain.ProductMarketing_;
import org.hoteia.qalingo.core.domain.ProductSku;
import org.hoteia.qalingo.core.domain.ProductSkuPrice_;
import org.hoteia.qalingo.core.domain.ProductSku_;
import org.hoteia.qalingo.core.domain.Retailer;
import org.hoteia.qalingo.core.domain.enumtype.FoUrls;
import org.hoteia.qalingo.core.fetchplan.FetchPlan;
import org.hoteia.qalingo.core.fetchplan.SpecificFetchMode;
import org.hoteia.qalingo.core.pojo.RequestData;
import org.hoteia.qalingo.core.service.CatalogCategoryService;
import org.hoteia.qalingo.core.service.ProductService;
import org.hoteia.qalingo.core.solr.response.ProductMarketingResponseBean;
import org.hoteia.qalingo.core.solr.service.ProductMarketingSolrService;
import org.hoteia.qalingo.core.solr.service.impl.AbstractSolrService;
import org.hoteia.qalingo.core.web.mvc.viewbean.CartViewBean;
import org.hoteia.qalingo.core.web.mvc.viewbean.SearchProductItemViewBean;
import org.hoteia.qalingo.core.web.servlet.ModelAndViewThemeDevice;
import org.hoteia.qalingo.core.web.servlet.view.RedirectView;
import org.hoteia.qalingo.web.mvc.controller.AbstractMCommerceController;
import org.hoteia.qalingo.web.mvc.form.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 */
@Controller("catalogSearchController")
public class CatalogSearchController extends AbstractMCommerceController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	public ProductService productMarketingService;
	
	@Autowired
	public ProductMarketingSolrService productMarketingSolrService;

    protected List<SpecificFetchMode> productSkuFetchPlans = new ArrayList<SpecificFetchMode>();;
    protected List<SpecificFetchMode> productMarketingFetchPlans = new ArrayList<SpecificFetchMode>();
    protected List<SpecificFetchMode> categoryVirtualFetchPlans = new ArrayList<SpecificFetchMode>();;

    public CatalogSearchController() {
        productSkuFetchPlans.add(new SpecificFetchMode(ProductSku_.productMarketing.getName()));
        productSkuFetchPlans.add(new SpecificFetchMode(ProductSku_.attributes.getName()));
        productSkuFetchPlans.add(new SpecificFetchMode(ProductSku_.prices.getName()));
        productSkuFetchPlans.add(new SpecificFetchMode(ProductSku_.prices.getName() + "." + ProductSkuPrice_.currency.getName()));
        productSkuFetchPlans.add(new SpecificFetchMode(ProductSku_.assets.getName()));
        
        productMarketingFetchPlans.add(new SpecificFetchMode(ProductMarketing_.productBrand.getName()));
        productMarketingFetchPlans.add(new SpecificFetchMode(ProductMarketing_.productMarketingType.getName()));
        productMarketingFetchPlans.add(new SpecificFetchMode(ProductMarketing_.attributes.getName()));
        productMarketingFetchPlans.add(new SpecificFetchMode(ProductMarketing_.productSkus.getName()));
        productMarketingFetchPlans.add(new SpecificFetchMode(ProductMarketing_.productSkus.getName() + "." + ProductSku_.prices.getName()));
        productMarketingFetchPlans.add(new SpecificFetchMode(ProductMarketing_.productSkus.getName() + "." + ProductSku_.prices.getName() + "." + ProductSkuPrice_.currency.getName()));
        productMarketingFetchPlans.add(new SpecificFetchMode(ProductMarketing_.productAssociationLinks.getName()));
        productMarketingFetchPlans.add(new SpecificFetchMode(ProductMarketing_.assets.getName()));
        
        categoryVirtualFetchPlans.add(new SpecificFetchMode(CatalogCategoryVirtual_.catalogCategories.getName()));
        categoryVirtualFetchPlans.add(new SpecificFetchMode(CatalogCategoryVirtual_.parentCatalogCategory.getName()));
        categoryVirtualFetchPlans.add(new SpecificFetchMode(CatalogCategoryVirtual_.attributes.getName()));
        categoryVirtualFetchPlans.add(new SpecificFetchMode(CatalogCategoryVirtual_.categoryMaster.getName()));
        categoryVirtualFetchPlans.add(new SpecificFetchMode(CatalogCategoryVirtual_.parentCatalogCategory.getName() + "." + CatalogCategoryVirtual_.categoryMaster.getName()));
        categoryVirtualFetchPlans.add(new SpecificFetchMode(CatalogCategoryVirtual_.categoryMaster.getName() + "." + CatalogCategoryMaster_.catalogCategoryType.getName()));
        categoryVirtualFetchPlans.add(new SpecificFetchMode(CatalogCategoryVirtual_.catalogCategoryProductSkuRels.getName()));
        categoryVirtualFetchPlans.add(new SpecificFetchMode(CatalogCategoryVirtual_.catalogCategoryProductSkuRels.getName() + "." + CatalogCategoryVirtualProductSkuRel_.pk.getName() +  "." + org.hoteia.qalingo.core.domain.CatalogCategoryVirtualProductSkuPk_.productSku.getName()));
        categoryVirtualFetchPlans.add(new SpecificFetchMode(CatalogCategoryVirtual_.assets.getName()));
    }
    
	@RequestMapping(value = FoUrls.CATALOG_SEARCH_URL, method = RequestMethod.GET)
	public ModelAndView search(final HttpServletRequest request, final Model model, @Valid SearchForm searchForm) throws Exception {
		
		ModelAndViewThemeDevice modelAndView = new ModelAndViewThemeDevice(getCurrentVelocityPath(request), FoUrls.CATALOG_SEARCH.getVelocityPage());
        final RequestData requestData = requestUtil.getRequestData(request);

		String url = requestUtil.getCurrentRequestUrl(request);
		
		String sessionKey = "PagedListHolder_Search_List_ProductMarketing_" + request.getSession().getId();
        int page = searchForm.getPage() - 1;
        String mode = request.getParameter(Constants.PAGE_VIEW_MODE);
        String sortBy = searchForm.getSortBy();
        String order = searchForm.getOrder();
		
		try {
			ProductMarketingResponseBean productMarketingResponseBean = null;
			if(searchForm.getPrice() != null){
				productMarketingResponseBean = productMarketingSolrService.searchProductMarketing(ProductMarketingResponseBean.PRODUCT_MARKETING_DEFAULT_SEARCH_FIELD, 
						searchForm.getText(), ProductMarketingResponseBean.PRODUCT_MARKETING_DEFAULT_FACET_FIELD, searchForm.getPrice().getStartValue(), searchForm.getPrice().getEndValue(), 
						searchForm.getCatalogCategoryList());
				ProductMarketingResponseBean nonCategoriesFilter = productMarketingSolrService.searchProductMarketing(ProductMarketingResponseBean.PRODUCT_MARKETING_DEFAULT_SEARCH_FIELD, 
						searchForm.getText(), ProductMarketingResponseBean.PRODUCT_MARKETING_DEFAULT_FACET_FIELD, searchForm.getPrice().getStartValue(), searchForm.getPrice().getEndValue());
				modelAndView.addObject(AbstractSolrService.SEARCH_FACET_FIELD_LIST, frontofficeViewBeanFactory.buildListViewBeanCatalogSearchFacet(requestData, nonCategoriesFilter));
			} else {
				productMarketingResponseBean = productMarketingSolrService.searchProductMarketing(ProductMarketingResponseBean.PRODUCT_MARKETING_DEFAULT_SEARCH_FIELD, 
														searchForm.getText(), ProductMarketingResponseBean.PRODUCT_MARKETING_DEFAULT_FACET_FIELD);
				modelAndView.addObject(AbstractSolrService.SEARCH_FACET_FIELD_LIST, frontofficeViewBeanFactory.buildListViewBeanCatalogSearchFacet(requestData, productMarketingResponseBean));
			}
	        
			PagedListHolder<SearchProductItemViewBean> productsViewBeanPagedListHolder;

			productsViewBeanPagedListHolder = initList(request, sessionKey, productMarketingResponseBean, new PagedListHolder<SearchProductItemViewBean>(), searchForm);
			
	        int pageCurrent = productsViewBeanPagedListHolder.getPage();
	        if (pageCurrent < page) { 
	        	for (int i = pageCurrent; i < page; i++) {
	        		productsViewBeanPagedListHolder.nextPage(); 
				}
	        } else if (pageCurrent > page) { 
	        	for (int i = page; i < pageCurrent; i++) {
		        	productsViewBeanPagedListHolder.previousPage(); 
				}
	        }
	        
			modelAndView.addObject(Constants.PAGINATION_PAGE_URL, url);
			modelAndView.addObject(Constants.PAGINATION_PAGE_PAGED_LIST_HOLDER, productsViewBeanPagedListHolder);
			modelAndView.addObject(Constants.SEARCH_TEXT, searchForm.getText());
			modelAndView.addObject(Constants.PAGINATION_PAGE_SIZE, productsViewBeanPagedListHolder.getPageSize());
			modelAndView.addObject(Constants.PAGINATION_SORT_BY, sortBy);
			modelAndView.addObject(Constants.PAGINATION_ORDER, order);
			modelAndView.addObject(Constants.PRICE_RANGE_PARAMETER, searchForm.getPrice());
			modelAndView.addObject(Constants.CATALOG_CATEGORIES_PARAMETER, searchForm.getCategoriesFilter());
			
		} catch (Exception e) {
			logger.error("SOLR Error", e);
			return displaySearch(request, model);
		}
		
		loadRecentProducts(request, requestData, model, new FetchPlan(categoryVirtualFetchPlans), new FetchPlan(productMarketingFetchPlans), new FetchPlan(productSkuFetchPlans));
        
        final Cart currentCart = requestData.getCart();
        final CartViewBean cartViewBean = frontofficeViewBeanFactory.buildViewBeanCart(requestUtil.getRequestData(request), currentCart);
        modelAndView.addObject(ModelConstants.CART_VIEW_BEAN, cartViewBean);
		
        overrideDefaultSeoPageTitleAndMainContentTitle(request, modelAndView, FoUrls.CATALOG_SEARCH.getKey());

        return modelAndView;
	}
	
    protected ModelAndView displaySearch(final HttpServletRequest request, final Model model) throws Exception {
        ModelAndViewThemeDevice modelAndView = new ModelAndViewThemeDevice(getCurrentVelocityPath(request), FoUrls.CATALOG_SEARCH.getVelocityPage());
        final RequestData requestData = requestUtil.getRequestData(request);

        modelAndView.addObject(ModelConstants.SEARCH_FORM, formFactory.buildSearchForm(requestData));

        loadRecentProducts(request, requestData, model, new FetchPlan(categoryVirtualFetchPlans), new FetchPlan(productMarketingFetchPlans), new FetchPlan(productSkuFetchPlans));

        final Cart currentCart = requestData.getCart();
        final CartViewBean cartViewBean = frontofficeViewBeanFactory.buildViewBeanCart(requestUtil.getRequestData(request), currentCart);
        modelAndView.addObject(ModelConstants.CART_VIEW_BEAN, cartViewBean);

        return modelAndView;
    }
    
	// TODO : Temporary
	
    @Autowired
    public ProductService productService;

    @Autowired
    private CatalogCategoryService catalogCategoryService;
    
    @RequestMapping(value = "/**/search-load-catalog-index.html", method = RequestMethod.GET)
    public ModelAndView loadIndex(final HttpServletRequest request, final HttpServletResponse response, ModelMap modelMap) throws Exception {
        final RequestData requestData = requestUtil.getRequestData(request);
        final MarketArea marketArea = requestData.getMarketArea();
        final Retailer retailer = requestData.getMarketAreaRetailer();

        List<ProductMarketing> products = productService.findProductMarketings();
        for (Iterator<ProductMarketing> iteratorProductMarketing = products.iterator(); iteratorProductMarketing.hasNext();) {
            ProductMarketing productMarketing = (ProductMarketing) iteratorProductMarketing.next();
            for (Iterator<ProductSku> iteratorProductSku = productMarketing.getProductSkus().iterator(); iteratorProductSku.hasNext();) {
                ProductSku productSku = (ProductSku) iteratorProductSku.next();
                List<CatalogCategoryVirtual> catalogCategories = catalogCategoryService.findVirtualCategoriesByProductSkuId(productSku.getId()); 
                productMarketingSolrService.addOrUpdateProductMarketing(productMarketing, catalogCategories, marketArea, retailer);
            }
        }

        return new ModelAndView(new RedirectView(urlService.generateUrl(FoUrls.CATALOG_SEARCH, requestUtil.getRequestData(request))));
    }
	
	private PagedListHolder<SearchProductItemViewBean> initList(final HttpServletRequest request, final String sessionKey, final ProductMarketingResponseBean productMarketingResponseBean,
			PagedListHolder<SearchProductItemViewBean> productsViewBeanPagedListHolder, final SearchForm searchForm) throws Exception{
		int pageSize = searchForm.getPageSize();
		String sortBy = searchForm.getSortBy();
        String order = searchForm.getOrder();
		List<SearchProductItemViewBean> searchtItems = frontofficeViewBeanFactory.buildListViewBeanSearchProductItem(requestUtil.getRequestData(request), productMarketingResponseBean);
		productsViewBeanPagedListHolder = new PagedListHolder<SearchProductItemViewBean>(searchtItems);
		productsViewBeanPagedListHolder.setPageSize(pageSize);
		productsViewBeanPagedListHolder.setSort(new MutableSortDefinition(sortBy, true, Constants.PAGE_ORDER_ASC.equalsIgnoreCase(order)));
		productsViewBeanPagedListHolder.resort();
        request.getSession().setAttribute(sessionKey, searchtItems);
        return productsViewBeanPagedListHolder;
	}
	
}