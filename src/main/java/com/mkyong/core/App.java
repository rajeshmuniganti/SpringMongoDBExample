package com.mkyong.core;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mkyong.model.Employee;
import com.mkyong.model.Organization;
import com.mkyong.model.User;

public class App {

	public static void main(String[] args) {

		ApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");
		boolean deepLogic = false;
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		Organization org = new Organization(001, "CGI", new Employee(140997, "Rajesh", "CGI"));
		mongoOperation.save(org);
		System.out.println("1. Org object : " + org);
		if (deepLogic) {
			findAndUpdate(mongoOperation);
		}
//
		Query searchQry = new Query(Criteria.where("orgId").is(001));
		Organization orgResult = mongoOperation.findOne(searchQry, Organization.class);
		System.out.println("Search result:" + orgResult);
	}

	private static void findAndUpdate(MongoOperations mongoOperation) {
		// query to search user
		Query searchUserQuery = new Query(Criteria.where("username").is("mkyong"));

		// find the saved user again.
		User savedUser = mongoOperation.findOne(searchUserQuery, User.class);
		System.out.println("2. find - savedUser : " + savedUser);

		// update password
		mongoOperation.updateFirst(searchUserQuery, Update.update("password", "new password"), User.class);

		// find the updated user object
		User updatedUser = mongoOperation.findOne(new Query(Criteria.where("username").is("mkyong")), User.class);

		System.out.println("3. updatedUser : " + updatedUser);

		// delete
		mongoOperation.remove(searchUserQuery, User.class);

		// List, it should be empty now.
		List<User> listUser = mongoOperation.findAll(User.class);
		System.out.println("4. Number of user = " + listUser.size());

	}

}