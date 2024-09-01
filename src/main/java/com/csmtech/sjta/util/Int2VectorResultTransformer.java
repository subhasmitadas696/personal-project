package com.csmtech.sjta.util;

import org.hibernate.transform.BasicTransformerAdapter;

public class Int2VectorResultTransformer extends BasicTransformerAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2609391956019956260L;

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		if (tuple.length == 1) {
			return tuple[0];
		}
		return super.transformTuple(tuple, aliases);
	}
}