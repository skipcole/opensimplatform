package org.usip.osp.baseobjects;

import javax.servlet.http.HttpServletRequest;

public interface CustomizeableSectionSubClass {

	public CustomizeableSection handleCustomizeSection(HttpServletRequest request, CustomizeableSection cs);
}
