package com.dontworry.crawler.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CrawlingOperation {
    getPlacesList("/data/places"),
    attractionList("/data/attractions/businesses"),
    petList("/data/placeList/businesses"),
    getBeautyList("/data/businesses"),
    getAccommodationList("/data/accommodationSearch/business"),
    getNxList("/data/businesses"),
    getRestaurants("/data/restaurants");

    public final String path;

}
