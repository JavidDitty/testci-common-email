package org.apache.commons.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.junit.*;

/**
* JUnit test class for the Email class in org.apache.commons.mail.
* 
* @author Javid Ditty
* @version 1.0
* @since 2023-19-03
*/
public class EmailTest {
	/**
	 * An email that is used to test methods in the Email class.
	 */
	private Email email;
	
	/**
	 * A generic email address.
	 */
	private final String GENERIC_ADDRESS = "user@host.domain";
	
	/**
	 * Initializes the email field before testing begins.
	 */
	@Before
	public void setup() {
		email = new EmailDummy();
	}
	
	/**
	 * Tests Email.addBcc(String... emails).
	 */
	@Test
	public void addBccTest() {
		try {
			String[] bccList = { GENERIC_ADDRESS };
			email = email.addBcc(bccList);
			String address = email.getBccAddresses().get(0).getAddress();
			Assert.assertEquals(bccList[0], "WRONG_ADDRESS");
		} catch (EmailException ignored) {
		}
	}
	
	/**
	 * Tests Email.addCc(String email).
	 */
	@Test
	public void addCcTest() {
		try {
			String cc = GENERIC_ADDRESS;
			email.addCc(cc);
			String address = email.getCcAddresses().get(0).getAddress();
			Assert.assertEquals(cc, address);
		} catch (EmailException ignored) {
		}
	}
	
	/**
	 * Tests Email.addHeader(String name, String value)
	 * when name = "" and value = "value".
	 * 
	 * @throws IllegalArgumentException when the name or value 
	 *         added to an email header is empty ("" or null).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addHeaderEmptyNameTest() {
		String name = "";
		String value = "value";
		
		email.addHeader(name, value);
	}
	
	/**
	 * Tests Email.addHeader(String name, String value)
	 * when name = "name" and value = "".
	 * 
	 * @throws IllegalArgumentException when the name or value 
	 *         added to an email header is empty ("" or null).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addHeaderEmptyValueTest() {
		String name = "name";
		String value = "";
		
		email.addHeader(name, value);
	}
	
	/**
	 * Tests Email.addHeader(String name, String value)
	 * when name = "name" and value = "value".
	 */
	@Test
	public void addHeaderNonEmptyTest() {
		try {
			String name = "name";
			String value = "value";
			email.addHeader(name, value);
			
			String headerValue = email.headers.get(name);
			Assert.assertEquals(value, headerValue);
		} catch (IllegalArgumentException ignored) {
		}
	}
	
	/**
	 * Tests Email.addReplyTo(String email, String name)
	 * when email = GENERIC_ADDRESS and name = "name",
	 * ensuring that the reply to address is correct.
	 */
	@Test
	public void addReplyToAddressTest() {
		try {
			String address = GENERIC_ADDRESS;
			String name = "name";
			
			email.addReplyTo(address, name);
			List<InternetAddress> replyToAddresses = email.getReplyToAddresses();
			String replyToAddress = replyToAddresses.get(0).getAddress();
			Assert.assertEquals(address, replyToAddress);	
		} catch (EmailException ignored) {
		}
	}
	
	/**
	 * Tests Email.addReplyTo(String email, String name)
	 * when email = GENERIC_ADDRESS and name = "name",
	 * ensuring that the reply to name is correct.
	 */
	@Test
	public void addReplyToNameTest() {
		try {
			String address = GENERIC_ADDRESS;
			String name = "name";
			
			email.addReplyTo(address, name);
			List<InternetAddress> replyToAddresses = email.getReplyToAddresses();
			String replyToName = replyToAddresses.get(0).getPersonal();
			Assert.assertEquals(name, replyToName);
		} catch (EmailException ignored) {
		}
	}
	
	/**
	 * Tests Email.buildMimeMessage() when email.message is not empty ("" or null).
	 * 
	 * @throws IllegalArgumentException when email.massage is not empty ("" or null).
	 */
	@Test(expected = IllegalStateException.class)
	public void buildMimeMessageNonEmptyMessageTest() {
		try {
			setupBuildMimeMessage();
			Properties properties = new Properties();
			Session session = Session.getInstance(properties);
			email.message = email.createMimeMessage(session);
			email.buildMimeMessage();
		} catch (EmailException ignored) {
		}
	}
	
	/**
	 * Tests Email.buildMimeMessage() when email.charset is not empty (not "" or null).
	 */
	@Test
	public void buildMimeMessageNonEmptyCharsetTest() {
		try {
			String subject = "subject";
			String charset = "UTF-8";
			
			setupBuildMimeMessage();
			email.setSubject(subject);
			email.setCharset(charset);
			email.buildMimeMessage();
			
			MimeMessage message = email.getMimeMessage();
			String messageSubject = message.getSubject();
			Assert.assertEquals(subject, messageSubject);
		} catch (EmailException ignored) {
		} catch(MessagingException ignored) {
		}
	}
	
	/**
	 * Tests Email.buildMimeMessage() when email.charset is empty ("" or null).
	 */
	@Test
	public void buildMimeMessageEmptyCharsetTest() {
		try {
			String subject = "subject";
			String charset = null;
			
			setupBuildMimeMessage();
			email.setSubject(subject);
			email.charset = charset;
			email.buildMimeMessage();
			
			MimeMessage message = email.getMimeMessage();
			String messageSubject = message.getSubject();
			Assert.assertEquals(subject, messageSubject);
		} catch (EmailException ignored) {
		} catch(MessagingException ignored) {
		}
	}
	
	/**
	 * Tests Email.buildMimeMessage() when email content is a 
	 * non-empty (not "" or null) String.
	 */
	@Test
	public void buildMimeMessageStringContentTest() {
		try {
			String content = "content";
			String contentType = EmailConstants.TEXT_PLAIN;
			
			setupBuildMimeMessage();
			email.setContent(content, contentType);
			email.buildMimeMessage();
			
			MimeMessage message = email.getMimeMessage();
			String messageContent = (String) message.getContent();
			Assert.assertEquals(content, messageContent);
		} catch (EmailException ignored) {
		} catch(MessagingException ignored) {
		} catch(IOException ignored) {
		}
	}
	
	/**
	 * Tests Email.buildMimeMessage() when email content 
	 * is a non-empty (not "" or null) non-String.
	 */
	@Test
	public void buildMimeMessageNonStringContentTest() {
		try {
			Object content = new Object();
			String contentType = null;
			
			setupBuildMimeMessage();
			email.setContent(content, contentType);
			email.buildMimeMessage();
			
			MimeMessage message = email.getMimeMessage();
			Object messageContent = message.getContent();
			Assert.assertEquals(content, messageContent);
		} catch (EmailException ignored) {
		} catch(MessagingException ignored) {
		} catch(IOException ignored) {
		}
	}
	
	/**
	 * Tests Email.buildMimeMessage() when email content type 
	 * is empty ("" or null).
	 */
	@Test
	public void buildMimeMessageEmptyContentTypeTest() {
		try {
			MimeMultipart content = new MimeMultipart();
			String contentType = null;
			
			setupBuildMimeMessage();
			email.setContent(content);
			email.updateContentType(contentType);
			email.buildMimeMessage();
			
			MimeMessage message = email.getMimeMessage();
			MimeMultipart messageContent = (MimeMultipart) message.getContent();
			Assert.assertEquals(content, messageContent);
		} catch (EmailException ignored) {
		} catch(MessagingException ignored) {
		} catch(IOException ignored) {
		}
	}
	
	/**
	 * Tests Email.buildMimeMessage() when email content type 
	 * is not empty (not "" or null).
	 */
	@Test
	public void buildMimeMessageNonEmptyContentTypeTest() {
		try {
			MimeMultipart content = new MimeMultipart();
			String contentType = EmailConstants.TEXT_PLAIN;
			
			setupBuildMimeMessage();
			email.setContent(content);
			email.updateContentType(contentType);
			email.buildMimeMessage();
			
			MimeMessage message = email.getMimeMessage();
			MimeMultipart messageContent = (MimeMultipart) message.getContent();
			Assert.assertEquals(content, messageContent);
		} catch (EmailException ignored) {
		} catch(MessagingException ignored) {
		} catch(IOException ignored) {
		}
	}
	
	/**
	 * Tests Email.buildMimeMessage() when email has a CC List
	 * whose size is greater than 0.
	 */
	@Test
	public void buildMimeMessageNonEmptyCCListTest() {
		try {
			InternetAddress address = new InternetAddress(GENERIC_ADDRESS);
			Collection<InternetAddress> addresses = new ArrayList<InternetAddress>();
			addresses.add(address);
			
			setupBuildMimeMessage();
			email.setCc(addresses);
			email.buildMimeMessage();
			
			MimeMessage message = email.getMimeMessage();
			InternetAddress[] messageRecipients = (InternetAddress[]) message.getRecipients(RecipientType.CC);
			Assert.assertEquals(address, messageRecipients[0]);
		} catch (EmailException ignored) {
		} catch(MessagingException ignored) {
		}
	}
	
	/**
	 * Tests Email.buildMimeMessage() when email has a BCC List
	 * whose size is greater than 0.
	 */
	@Test
	public void buildMimeMessageNonEmptyBccListTest() {
		try {
			InternetAddress address = new InternetAddress(GENERIC_ADDRESS);
			Collection<InternetAddress> addresses = new ArrayList<InternetAddress>();
			addresses.add(address);
			
			setupBuildMimeMessage();
			email.setBcc(addresses);
			email.buildMimeMessage();
			
			MimeMessage message = email.getMimeMessage();
			InternetAddress[] messageRecipients = (InternetAddress[]) message.getRecipients(RecipientType.BCC);
			Assert.assertEquals(address, messageRecipients[0]);
		} catch (EmailException ignored) {
		} catch(MessagingException ignored) {
		}
	}
	
	/**
	 * Tests Email.getHostName() when email has hostname and a mail session.
	 */
	@Test
	public void getHostNameWithSessionTest() {
		String testHostname = "hostname";
		
		Properties properties = new Properties();
		properties.setProperty(Email.MAIL_HOST, testHostname);
		Session session = Session.getInstance(properties);
		email.setMailSession(session);
		
		String emailHostname = email.getHostName();
		Assert.assertEquals(testHostname, emailHostname);
	}
	
	/**
	 * Tests Email.getHostName() when email has a hostname 
	 * but does not have a mail session.
	 */
	@Test
	public void getHostNameWithoutSessionTest() {
		String testHostname = "hostname";
		email.setHostName(testHostname);
		
		String emailHostname = email.getHostName();
		Assert.assertEquals(testHostname, emailHostname);
	}

	/**
	 * Tests Email.getMailSession() when Email.MAIL_HOST is not empty (not "" or null).
	 */
	@Test
	public void getMailSessionWithMailHostTest() {
		try {
			String testHostname = "hostname";
			
			email.setHostName(null);
			System.setProperty(Email.MAIL_HOST, testHostname);
			email.getMailSession();
			
			String emailHostname = email.getHostName();
			Assert.assertEquals(testHostname, emailHostname);
		} catch (EmailException ignored) {
		}
	}
	
	/**
	 * Tests Email.getMailSession() when Email.MAIL_HOST is empty ("" or null).
	 * 
	 * @throws EmailException when Email.MAIL_HOST is empty ("" or null).
	 */
	@Test(expected = EmailException.class)
	public void getMailSessionWithoutMailHostTest() throws EmailException {
		String testHostname = "";
		email.setHostName(null);
		System.setProperty(Email.MAIL_HOST, testHostname);
		email.getMailSession();
	}

	/**
	 * Tests Email.getSentDate() when email.sentDate == null.
	 */
	@Test
	public void getSentDateNullDateTest() {
		email.setSentDate(null);
		
		Date sentDate = email.getSentDate();
		Date testDate = new Date();
		boolean equalDate = testDate.equals(sentDate);
		Assert.assertEquals(true, equalDate);
	}
	
	/**
	 * Tests Email.getSentDate() when email.sentDate != null.
	 */
	@Test
	public void getSentDateNonNullDateTest() {
		Date testDate = new Date();
		email.setSentDate(testDate);
		
		Date sentDate = email.getSentDate();
		Assert.assertEquals(testDate, sentDate);
	}

	/**
	 * Tests Email.getSocketConnectionTimeout().
	 */
	@Test
	public void getSocketConnectionTimeoutTest() {
		int testTimeout = 100;
		email.setSocketConnectionTimeout(testTimeout);
		
		int socketTimeout = email.getSocketConnectionTimeout();
		Assert.assertEquals(testTimeout, socketTimeout);
	}
	
	/**
	 * Tests Email.setFrom(String email) when email = GENERIC_ADDRESS.
	 */
	@Test
	public void setFromTest() {
		try {
			String testAddress = GENERIC_ADDRESS;
			email.setFrom(testAddress);
			
			String fromAddress = email.getFromAddress().getAddress();
			Assert.assertEquals(testAddress, fromAddress);
		} catch (EmailException ignored) {
		}
	}
	
	/**
	 * Uninitialize email when tests are complete.
	 */
	@After
	public void teardown() {
		email = null;
	}
	
	/**
	 * Setup email for email.buildMimeMessage() tests.
	 * @throws EmailException when aHostName passed to email.setHostName(String aHostName) is invalid.
	 */
	private void setupBuildMimeMessage() throws EmailException {
		email.setFrom(GENERIC_ADDRESS);
		email.addTo(GENERIC_ADDRESS);
		email.setHostName("hostname");
	}
}

