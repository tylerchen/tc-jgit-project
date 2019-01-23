//import java.util.Date;
//
//import org.iff.infra.util.GsonHelper;
//
//import org.iff.datarest.core.model.DataSourceModel;
//import org.iff.datarest.core.model.QueryStatementModel;
//import org.iff.datarest.core.service.BaseDefaultRestHandler;
//
///*******************************************************************************
// * Copyright (c) Oct 14, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
// * All rights reserved.
// *
// * Contributors:
// *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
// ******************************************************************************/
//
///**
// * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
// * @since Oct 14, 2016
// */
//public class AddData {
//
//	public static void main(String[] args) {
//		{
//			DataSourceModel model = new DataSourceModel();
//			{
//				model.setName("test");
//				model.setUser("iff");
//				model.setPassword("iff");
//				model.setDriver("com.mysql.jdbc.Driver");
//				model.setUrl(
//						"jdbc:mysql://localhost:3306/new_qdp_0201?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false");
//				model.setValidationQuery("select 1");
//				model.setMaxConnection(3);
//				model.setInitConnection(3);
//				model.setCreateTime(new Date());
//			}
//			//			String post = HttpHelper.post("http://localhost:8989/default/datasource_update",
//			//					"datasource=" + BaseDefaultRestHandler.urlEncode(GsonHelper.toJsonString(model)));
//			System.out.println(GsonHelper.toJsonString(model));
//			System.out.println("datasource=" + BaseDefaultRestHandler.urlEncode(GsonHelper.toJsonString(model)));
//		}
//		{
//			QueryStatementModel model = new QueryStatementModel();
//			{
//				model.setName("test");
//				model.setSelectBody("*");
//				model.setFromBody("auth_account");
//				model.setWhereBody("[AND LOGIN_EMAIL=:string_loginEmail] [AND ID=:id]");
//				model.setCreateTime(new Date());
//			}
//			//			String post = HttpHelper.post("http://localhost:8989/default/querystatement_update",
//			//					"queryStatement=" + BaseDefaultRestHandler.urlEncode(GsonHelper.toJsonString(model)));
//			System.out.println(GsonHelper.toJsonString(model));
//			System.out.println("queryStatement=" + BaseDefaultRestHandler.urlEncode(GsonHelper.toJsonString(model)));
//		}
//	}
//}
