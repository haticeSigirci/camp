package tr.org.lkd.lyk2015.camp.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ThymeleafLayoutInterceptor extends HandlerInterceptorAdapter {

	public static final String DEFAULT_LAYOUT = "layout/main";
	private static final String DEFAULT_VIEW_ATTRIBUTE_NAME = "layout_main";

	private String defaultLayout = DEFAULT_LAYOUT;
	private String viewAttributeName = DEFAULT_VIEW_ATTRIBUTE_NAME;

	public void setDefaultLayout(String defaultLayout) {
		Assert.hasLength(defaultLayout);
		this.defaultLayout = defaultLayout;
	}

	public void setViewAttributeName(String viewAttributeName) {
		Assert.hasLength(this.defaultLayout);
		this.viewAttributeName = viewAttributeName;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		if (modelAndView == null || !modelAndView.hasView()) {
			return;
		}

		String originalViewName = modelAndView.getViewName(); // view get from
																// controller

		if (this.isRedirectOrForward(originalViewName)) {

			return; // continue

		}

		// intercepter is changing the controller action, in this way we can
		// choose the second url

		String layoutName = this.getLayoutName(handler);
		modelAndView.setViewName(layoutName);
		modelAndView.addObject(this.viewAttributeName, originalViewName);

	}

	private String getLayoutName(Object handler) {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Layout layout = this.getMethodOrTypeAnnotation(handlerMethod);
		if (layout == null) {
			return this.defaultLayout;
		} else {
			return layout.value();
		}
	}

	private Layout getMethodOrTypeAnnotation(HandlerMethod handlerMethod) {
		Layout layout = handlerMethod.getMethodAnnotation(Layout.class);
		if (layout == null) {
			return handlerMethod.getBeanType().getAnnotation(Layout.class);
		}
		return layout;
	}

	private boolean isRedirectOrForward(String originalViewName) {
		return originalViewName.startsWith("redirect:") || originalViewName.startsWith("forward:");
	}
}
