package com.kudlwork.netty.common.parser.annotation;

import static com.kudlwork.netty.common.CommonConstants.DEFAULT_CHARSET;

public enum  VariableIndexType {
	BEGIN {
		public int getVariableCount(String content, Integer startIndex, Integer size) {
			int cnt = 0;
			StringBuffer sb = new StringBuffer();
			char[] charGroup = content.toCharArray();

			for (char ch : charGroup) {
				cnt += String.valueOf(ch).getBytes(DEFAULT_CHARSET).length;

				if (cnt > startIndex + size) {
					break;
				}

				if (cnt >= startIndex && cnt <= startIndex + size - 1) {
					sb.append(ch);
				}
			}



			return Integer.valueOf(sb.toString().trim());
		}
	},
	END {
		public int getVariableCount(String content, Integer startIndex, Integer size) {
			int cnt = 0;
			StringBuffer variableCount = new StringBuffer();
			char[] charGroup = new StringBuffer(content).reverse().toString().toCharArray();

			for (char ch : charGroup) {
				cnt += String.valueOf(ch).getBytes(DEFAULT_CHARSET).length;

				if (cnt > startIndex + size) {
					break;
				}

				if (cnt >= startIndex && cnt <= startIndex + size - 1) {
					variableCount.append(ch);
				}
			}

			return Integer.valueOf(variableCount.reverse().toString().trim());
		}
	},
	NONE {
		public int getVariableCount(String content, Integer startIndex, Integer size) {
			return 0;
		}
	};

	public abstract int getVariableCount(String content, Integer startIndex, Integer size);
}
