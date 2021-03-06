/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.8.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2014
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.web.mvc.viewbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ProductMarketingViewBean extends AbstractViewBean implements Serializable {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 9190853998911450184L;

	protected Long id;
	protected String code;
	protected String name;
    protected String description;

    protected String i18nName;
    protected String i18nDescription;

	protected int positionItem;

	protected String backgroundImage;
	protected String carouselImage;
	protected String iconImage;

	protected boolean isDefault;
	protected boolean featured;

	protected ProductBrandViewBean brand;

    private Map<String, AttributeValueViewBean> globalAttributes = new HashMap<String, AttributeValueViewBean>();
    private Map<String, AttributeValueViewBean> marketAreaAttributes = new HashMap<String, AttributeValueViewBean>();

    protected List<ProductSkuViewBean> productSkus = new ArrayList<ProductSkuViewBean>();
    protected List<ProductAssociationLinkViewBean> productAssociationLinks = new ArrayList<ProductAssociationLinkViewBean>();
    protected List<AssetViewBean> assets = new ArrayList<AssetViewBean>();

    protected CustomerProductRatesViewBean customerProductRates;

    protected String detailsUrl;
    protected String editUrl;

    protected String brandDetailsUrl;
    protected String brandLineDetailsUrl;

    protected String addSkuUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
        return name;
    }
	
	public void setName(String name) {
        this.name = name;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
    public String getI18nName() {
        if(StringUtils.isNotEmpty(i18nName)){
            return i18nName;
        }
        return name;
    }
    
    public void setI18nName(String i18nName) {
        this.i18nName = i18nName;
    }
    
    public String getI18nDescription() {
        if(StringUtils.isNotEmpty(i18nDescription)){
            return i18nDescription;
        }
        return description;
    }
    
    public void setI18nDescription(String i18nDescription) {
        this.i18nDescription = i18nDescription;
    }

	public int getPositionItem() {
		return positionItem;
	}

	public void setPositionItem(int positionItem) {
		this.positionItem = positionItem;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public String getCarouselImage() {
		return carouselImage;
	}

	public void setCarouselImage(String carouselImage) {
		this.carouselImage = carouselImage;
	}

	public String getIconImage() {
		return iconImage;
	}

	public void setIconImage(String iconImage) {
		this.iconImage = iconImage;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isSalable() {
	    if(productSkus != null){
	        for (Iterator<ProductSkuViewBean> iterator = productSkus.iterator(); iterator.hasNext();) {
	            ProductSkuViewBean productSkuViewBean = (ProductSkuViewBean) iterator.next();
	            if(productSkuViewBean.isSalable){
	                return true;
	            }
	        }
	    }
        return false;
    }

    public boolean isFeatured() {
		return featured;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

	public ProductBrandViewBean getBrand() {
		return brand;
	}

	public void setBrand(ProductBrandViewBean brand) {
		this.brand = brand;
	}

    public Map<String, AttributeValueViewBean> getGlobalAttributes() {
        return globalAttributes;
    }
    
    public AttributeValueViewBean getGlobalAttribute(String code) {
        if(globalAttributes != null){
            AttributeValueViewBean attributeValue = globalAttributes.get(code);
            if(attributeValue != null){
                return attributeValue;
            }
        }
        return null;
    }
    
    public void setGlobalAttributes(Map<String, AttributeValueViewBean> globalAttributes) {
        this.globalAttributes = globalAttributes;
    }
    
    public Map<String, AttributeValueViewBean> getMarketAreaAttributes() {
        return marketAreaAttributes;
    }
    
    public AttributeValueViewBean getMarketAreaAttribute(String code) {
        if(marketAreaAttributes != null){
            AttributeValueViewBean attributeValue = marketAreaAttributes.get(code);
            if(attributeValue != null){
                return attributeValue;
            }
        }
        return null;
    }
    
    public void setMarketAreaAttributes(Map<String, AttributeValueViewBean> marketAreaAttributes) {
        this.marketAreaAttributes = marketAreaAttributes;
    }

	public List<ProductSkuViewBean> getProductSkus() {
		return productSkus;
	}

	public void setProductSkus(List<ProductSkuViewBean> productSkus) {
		this.productSkus = productSkus;
	}

	public List<ProductAssociationLinkViewBean> getProductAssociationLinks() {
		return productAssociationLinks;
	}

	public void setProductAssociationLinks(
			List<ProductAssociationLinkViewBean> productAssociationLinks) {
		this.productAssociationLinks = productAssociationLinks;
	}

	public List<AssetViewBean> getAssets() {
		return assets;
	}

	public void setAssets(List<AssetViewBean> assets) {
		this.assets = assets;
	}

	public String getAddToCartUrl() {
		if (productSkus != null) {
			for (Iterator<ProductSkuViewBean> iterator = productSkus.iterator(); iterator
					.hasNext();) {
				ProductSkuViewBean productSkuViewBean = (ProductSkuViewBean) iterator
						.next();
				if (productSkuViewBean.isDefault()) {
					return productSkuViewBean.getAddToCartUrl();
				}
			}
			if (!productSkus.isEmpty()) {
				ProductSkuViewBean productSkuViewBean = productSkus.get(0);
				return productSkuViewBean.getAddToCartUrl();
			}
		}
		return null;
	}

	public String getSkuCode() {
		if (productSkus != null) {
			for (Iterator<ProductSkuViewBean> iterator = productSkus.iterator(); iterator.hasNext();) {
				ProductSkuViewBean productSkuViewBean = (ProductSkuViewBean) iterator.next();
				if (productSkuViewBean.isDefault()) {
					return productSkuViewBean.getCode();
				}
			}
			if (!productSkus.isEmpty()) {
				ProductSkuViewBean productSkuViewBean = productSkus.get(0);
				return productSkuViewBean.getCode();
			}
		}
		return null;
	}

	public String getAddToWishlistUrl() {
		if (productSkus != null) {
			for (Iterator<ProductSkuViewBean> iterator = productSkus.iterator(); iterator
					.hasNext();) {
				ProductSkuViewBean productSkuViewBean = (ProductSkuViewBean) iterator
						.next();
				if (productSkuViewBean.isDefault()) {
					return productSkuViewBean.getAddToWishlistUrl();
				}
			}
			if (!productSkus.isEmpty()) {
				ProductSkuViewBean productSkuViewBean = productSkus.get(0);
				return productSkuViewBean.getAddToWishlistUrl();
			}
		}
		return null;
	}

	public String getPriceWithCurrencySign() {
		if (productSkus != null) {
			for (Iterator<ProductSkuViewBean> iterator = productSkus.iterator(); iterator
					.hasNext();) {
				ProductSkuViewBean productSkuViewBean = (ProductSkuViewBean) iterator
						.next();
				if (productSkuViewBean.isDefault()) {
					if (productSkuViewBean.getPriceWithCurrencySign() != null) {
						return productSkuViewBean.getPriceWithCurrencySign();
					}
				}
			}
			if (!productSkus.isEmpty()) {
				ProductSkuViewBean productSkuViewBean = productSkus.get(0);
				if (productSkuViewBean != null
						&& productSkuViewBean.getPriceWithCurrencySign() != null) {
					return productSkuViewBean.getPriceWithCurrencySign();
				}
			}
		}
		return null;
	}

    public CustomerProductRatesViewBean getCustomerProductRates() {
        return customerProductRates;
    }

    public void setCustomerProductRates(CustomerProductRatesViewBean customerProductRatesViewBean) {
        this.customerProductRates = customerProductRatesViewBean;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

	public void setDetailsUrl(String detailsUrl) {
		this.detailsUrl = detailsUrl;
	}

	public String getEditUrl() {
		return editUrl;
	}

    public void setEditUrl(String editUrl) {
        this.editUrl = editUrl;
    }

    public String getBrandDetailsUrl() {
        return brandDetailsUrl;
    }

    public void setBrandDetailsUrl(String brandDetailsUrl) {
        this.brandDetailsUrl = brandDetailsUrl;
    }

    public String getBrandLineDetailsUrl() {
        return brandLineDetailsUrl;
    }

    public void setBrandLineDetailsUrl(String brandLineDetailsUrl) {
        this.brandLineDetailsUrl = brandLineDetailsUrl;
    }

    public String getAddSkuUrl() {
        return addSkuUrl;
    }

    public void setAddSkuUrl(String addSkuUrl) {
        this.addSkuUrl = addSkuUrl;
    }

}