package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import Driver.BaseClass;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import utilites.DataProviders;

public class TC003_LoginDDT extends BaseClass {
	
	@Test(dataProvider="LoginData", dataProviderClass=DataProviders.class,groups="Datadriven")

	public void verify_LoginDDT(String email, String pwd, String exp) throws InterruptedException
	{
		logger.info("**Starting TC003_LoginDDT**");
		
		try
		{
		// HomePage
	        HomePage hp = new HomePage(driver);
	        hp.clickMyAccount();
	        hp.clickLogin();
	        
	   //Login page
	        LoginPage lp = new LoginPage(driver);
	        lp.setEmail(email);
	        lp.setPassword(pwd);
	        lp.clickLogin();
	        
	    // MyAccount  
	        MyAccountPage macc=new MyAccountPage(driver);
	        boolean targetPage=macc.isMyAccountPageExists();
	        
	        /*
	        Data is valid - Login success  → Test PASS → Logout
	        - Login failed   → Test FAIL

	        Data is invalid- Login success  → Test FAIL → Logout
	        - Login failed   → Test PASS
	        */
		
	        if(exp.equalsIgnoreCase("Valid"));
	        {
	        	if( targetPage==true)
	        	{
	        		macc.clickLogout();
	        		Assert.assertTrue(true);
	        	}
	        	else
	        	{
	        		Assert.assertTrue(false);
	        	}
	        if(exp.equalsIgnoreCase("Invalid"))
	        {
	        	if( targetPage==true)
	        	{
	        		macc.clickLogout();
	        		Assert.assertTrue(false);
	        }
	        	else
	        	{
	        		Assert.assertTrue(true);
	}
	}
	        }
		}
	        catch(Exception e)
	        {
	        	Assert.fail();
	        }
		Thread.sleep(3000);
	        logger.info("**Finished TC003_LoginDDT**");
	        }
}

