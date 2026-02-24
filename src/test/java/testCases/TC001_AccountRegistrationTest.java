package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import Driver.BaseClass;
import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;

public class TC001_AccountRegistrationTest extends BaseClass {

    @Test(groups={"Regression","Master"})
    public void verify_account_registration() {
    	
    	logger.info("**Starting TC001_AccountRegistrationTes**");
try
{
        HomePage hp = new HomePage(driver);
        hp.clickMyAccount();
        hp.clickRegister();

        AccountRegistrationPage regpage =
                new AccountRegistrationPage(driver);
        
logger.info("Providing custumer detaild..");
        regpage.setFirstName(randomString().toUpperCase());
        regpage.setLastName(randomString().toUpperCase());
        regpage.setEmail(randomString() + "@gmail.com");
        regpage.setTelephone(randomNumber());

        String password = randomAlphaNumeric();
        regpage.setPassword(password);
        regpage.setConfirmPassword(password);

        regpage.setPrivacyPolicy();
        regpage.clickContinue();
        
        logger.info("Validate expected message..");
        String confmsg = regpage.getConfirmationMsg();
        if (confmsg.equals("Your Account Has Been Created!"))
        {
        	Assert.assertTrue(true);	
        }
        else
        {
        	logger.error("Test failed..");
        	logger.debug("Debug logs..");
        	Assert.assertTrue(false);
        	
        }
        
        Assert.assertEquals(confmsg, "Your Account Has Been Created!");
}
catch(Exception e)
{
	Assert.fail();
}
logger.info("**Finished TC001_AccountRegistrationTes**");
}
}



