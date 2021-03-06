package fr.soat.socialnetwork.ui;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Optional;

import fr.soat.socialnetwork.bo.IUser;
import fr.soat.socialnetwork.common.services.encryption.EncryptionServiceException;
import fr.soat.socialnetwork.common.services.encryption.IEncryptionService;

@RunWith(MockitoJUnitRunner.class)
public class RememberMeServiceTest {

	@Mock
	private IRememberMeCookieManager cookieManager;

	@Mock
	private IEncryptionService encryptionService;

	private IRememberMeService rememberMeService;

	private static final String userLogin = "login";
	private static final String userPassword = "password";
	private static final String encryptedPassword = "encryptedPassword";

	private void createService()
	{
		rememberMeService = new RememberMeService(cookieManager);
	}

	@Test
	public void shouldStoreUserNameInCookie() throws EncryptionServiceException
	{
		// given
		createService();
		stubDependencies();

		IUser user = createUser();

		// when
		rememberMeService.rememberMe(user);

		// then
		verify(cookieManager).addCookie(RememberMeService.NAME_COOKIE_PREFIX,
				userLogin);
	}

	private IUser createUser() {
		IUser user = mock(IUser.class);
		when
			(user.getLogin()).
		thenReturn
			(userLogin);
		when
			(user.getPassword()).
		thenReturn
			(userPassword);
		return user;
	}

	private void stubDependencies() throws EncryptionServiceException
	{
		stubCookieManager();
		stubEncryptionService();
	}

	@Test
	public void shouldGetUserNameFromCookie() throws EncryptionServiceException
	{
		// given
		createService();

		stubDependencies();

		// when
		Optional<IRememberedUser> rememberedUser = rememberMeService.getRememberedUser();

		// then
		assertThat(rememberedUser.get().getLogin(), is(equalTo(userLogin)));
	}

	private void stubCookieManager() {
		when
			(cookieManager.getCookieValue(RememberMeService.NAME_COOKIE_PREFIX)).
		thenReturn
			(userLogin);
		when
			(cookieManager.getCookieValue(RememberMeService.PASS_COOKIE_PREFIX)).
		thenReturn
			(new String(Base64.encodeBase64(encryptedPassword.getBytes())));
	}

	private void stubEncryptionService() throws EncryptionServiceException
	{
		when
			(encryptionService.encrypt(userPassword)).
		thenReturn
			(encryptedPassword);
		when
			(encryptionService.decrypt(encryptedPassword)).
		thenReturn
			(userPassword);
	}

	@Test
	public void shouldStoreUserEncryptedPasswordInCookieAsBase64Encoded() throws EncryptionServiceException
	{
		// given
		createService();
		stubDependencies();

		IUser user = createUser();

		// when
		rememberMeService.rememberMe(user);

		// then
		String encodedBase64Password = new String(Base64.encodeBase64(encryptedPassword.getBytes()));
		verify(cookieManager).addCookie(RememberMeService.PASS_COOKIE_PREFIX,
				encodedBase64Password);
	}

	@Test
	public void shouldGetUserPasswordFromCookie() throws EncryptionServiceException
	{
		// given
		createService();
		stubDependencies();

		// when
		Optional<IRememberedUser> rememberedUser = rememberMeService.getRememberedUser();

		// then
		assertThat(rememberedUser.get().getPassword(), is(equalTo(userPassword)));
	}

	@Test
	public void shouldNotReturnValidRememberedUserWhenCookiesNotFound() throws EncryptionServiceException
	{
		// given
		createService();

		// when
		Optional<IRememberedUser> rememberedUser = rememberMeService.getRememberedUser();

		// then
		assertThat(rememberedUser.isPresent(), is(equalTo(false)));
	}

	@Test
	public void shouldEncryptPasswordToCookie() throws EncryptionServiceException
	{
		// given
		createService();
		stubDependencies();

		IUser user = createUser();

		// when
		rememberMeService.rememberMe(user);

		// then
		verify(encryptionService).encrypt(userPassword);
	}

	@Test
	public void shouldDecryptPasswordFromCookie() throws EncryptionServiceException
	{
		// given
		createService();
		stubDependencies();
		stubCookieManager();

		// when
		rememberMeService.getRememberedUser();

		// then
		verify(encryptionService).decrypt(encryptedPassword);
	}

	@Test
	public void shouldRemoveAllCookies()
	{
		// given
		createService();
		stubCookieManager();
		IUser user = createUser();

		// when
		rememberMeService.forgetMe(user);

		// then
		verify(cookieManager).removeCookie(RememberMeService.NAME_COOKIE_PREFIX);
		verify(cookieManager).removeCookie(RememberMeService.PASS_COOKIE_PREFIX);
	}
}
