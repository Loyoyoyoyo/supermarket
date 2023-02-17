package com.lolo.supermarket.service;

import com.lolo.supermarket.entity.*;
import com.lolo.supermarket.exception.NotEnoughException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface GoodService {
    Goods[] selectAll();

    Goods selectById(Goods goods);

    List<Goods> selectByType(Goods goods);

    List<Goods> selectByName(GoodRetrieveName goodRetrieveName);

    int addGood(Goods good);

    int deleteById(Goods goods);

    int updateWeight(Goods goods);

    int updateName(Goods goods);

    int updateStock(Goods goods);

    int createCarGood(GoodCar goodCar, HttpServletRequest httpServletRequest);

    int updateCarGoodNum(GoodCar goodCar, HttpServletRequest httpServletRequest);

    GoodCarSum goodCarSum(HttpServletRequest httpServletRequest,List<GoodCar> goodCars);
    void orders(List<GoodCar> goodCar, HttpServletRequest httpServletRequest) throws NotEnoughException;

    List<List<Orders>> retrieveOrders(HttpServletRequest httpServletRequest);

    List<Orders> retrieveAllOrders(Orders orders);


}
