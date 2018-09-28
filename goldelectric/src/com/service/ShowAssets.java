package com.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.AssetsDAO;
import com.entity.Assets;

/**
 * Servlet implementation class ShowAssets
 */
@WebServlet("/ShowAsset")
public class ShowAssets extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowAssets() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		//String user_id = request.getParameter("user_id");
		//int userId = Integer.parseInt(user_id);
		int userId=	(int) request.getSession().getAttribute("userId");
		AssetsDAO a = new AssetsDAO();
		List<Assets> list = a.getAllById(userId);
		Date createtime;//
		Date endtime;
		double rxbje=0;   //日息宝金额
		double yxbje=0;	 //月息宝金额
		double nxbje=0;  //年 息宝金额
		
		double rrate=0; //日息宝利率
		double yrate=0;///月息宝利率
		double nrate=0;//年息宝利率
		
		double rlixi=0; //日息宝总利息
		double ylixi=0;///月息宝总利息
		double nlixi=0;//年息宝总利息
		
		double dz=0;//待赚利息
		
		double djje = 0;//冻结金额=
		
		double zz=0;//总已赚的利息
		double rz=0;//日息宝已赚的利息
		double yz=0;
		double nz=0;
		
		int rcount = 0;
		int ycount=0;;
		int ncount=0;;

		
		int pro_id;
		
		double sum1 = 0;//总本金
		double sum2 = 0;//总利息
		for (int i = 0; i < list.size(); i++) {
			createtime=list.get(i).getCreatetime();
			endtime=list.get(i).getEndtime();
			pro_id=list.get(i).getProduct_id();
			
			if(pro_id==1||pro_id==2||pro_id==3){		//日息宝投资总额
				rrate = Double.parseDouble(list.get(i).getRate());
				rlixi+=list.get(i).getTotal()*(rrate/100/365*((endtime.getTime()-createtime.getTime())/(1000*3600*24)));  //日息宝总利息
				rz+=list.get(i).getTotal()*(rrate/100/365*((new Date().getTime()-createtime.getTime())/(1000*3600*24)));//一种日息宝已经赚的利息+=
			if(new Date().getTime()<endtime.getTime()){
				djje+=list.get(i).getTotal();     //所有冻结金额加（未到期的）
			}
				rlixi=	new BigDecimal(rlixi).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				
				rxbje+=list.get(i).getTotal();                 //日息宝总本金
				rcount++;
			}
			
			if(pro_id==4||pro_id==5||pro_id==6){		//月息宝投资总额
				yrate = Double.parseDouble(list.get(i).getRate());
				ylixi+=list.get(i).getTotal()*(yrate/100/365*((endtime.getTime()-createtime.getTime())/(1000*3600*24)));  //月息宝总利息
				yz+=list.get(i).getTotal()*(yrate/100/365*((new Date().getTime()-createtime.getTime())/(1000*3600*24)));//一种月息宝已经赚的利息+=
				ylixi =new BigDecimal(ylixi).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				if(new Date().getTime()<endtime.getTime()){
					djje+=list.get(i).getTotal();     //所有冻结金额加（未到期的）
				}
				yxbje+=list.get(i).getTotal();
				ycount++;
			}
			
			if(pro_id==7||pro_id==8||pro_id==9){		//年息宝投资总额
				nrate = Double.parseDouble(list.get(i).getRate());
				nlixi+=list.get(i).getTotal()*(nrate/100)*((endtime.getTime()-createtime.getTime())/(1000*3600*24)/365);  //年息宝总利息
				nz+=list.get(i).getTotal()*(nrate/100/365*((new Date().getTime()-createtime.getTime())/(1000*3600*24)));//一种年息宝已经赚的利息+=
				nlixi = new BigDecimal(nlixi).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				nxbje+=list.get(i).getTotal();
				if(new Date().getTime()<endtime.getTime()){
					djje+=list.get(i).getTotal();     //所有冻结金额加（未到期的）
				}
				ncount++;
			}
			
//			total = list.get(i).getTotal();
//			
//			rate = Double.parseDouble(list.get(i).getRate());
//			sum1 += total;
//			sum2 += total*(rate/100);
			
		}
		//request.setAttribute("sum1", sum1);
		//request.setAttribute("sum2", sum2);
		sum1=rxbje+yxbje+nxbje;
		sum2=rlixi+ylixi+nlixi;
		zz=rz+yz+nz;//总赚利息（已赚利息）= 日总赚（已赚）+月+年
		dz=sum2-zz;//待赚利息=总利息-已赚利息
		rz=	new BigDecimal(rz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		yz=	new BigDecimal(yz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		nz=	new BigDecimal(nz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		sum2=	new BigDecimal(sum2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		sum1=	new BigDecimal(sum1).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		zz=	new BigDecimal(zz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		dz=	new BigDecimal(dz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//		List<Double> resultList = new ArrayList();
//		resultList.add(sum1);	
//		resultList.add(sum2);
//		resultList.add(rxbje);
//		resultList.add(yxbje);
//		resultList.add(nxbje);
//		resultList.add(rlixi);
//		resultList.add(ylixi);
//		resultList.add(nlixi);
//		resultList.add(rcount);
//		resultList.add(ycount);
//		resultList.add(ncount);
//		resultList.add(zz);
//		resultList.add(dz);
//		resultList.add(rz);
//		resultList.add(yz);
//		resultList.add(nz);
//		resultList.add(djje);
		//	response.getWriter().print(resultList);
		request.setAttribute("sum1", sum1);
		request.setAttribute("sum2", sum2);
		request.setAttribute("rxbje", rxbje);
		request.setAttribute("yxbje", yxbje);
		request.setAttribute("nxbje", nxbje);
		System.out.println(sum2);
		request.setAttribute("rlixi", rlixi);
		request.setAttribute("ylixi",ylixi );
		request.setAttribute("nlixi",nlixi );
		request.setAttribute("rcount", rcount);
		request.setAttribute("ycount", ycount);
		request.setAttribute("ncount", ncount);
		request.setAttribute("zz", zz);
		request.setAttribute("dz", dz);
		request.setAttribute("rz",rz );
		request.setAttribute("yz", yz);
		request.setAttribute("nz", nz);
		request.setAttribute("djje", djje);
		request.getRequestDispatcher("wd.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
