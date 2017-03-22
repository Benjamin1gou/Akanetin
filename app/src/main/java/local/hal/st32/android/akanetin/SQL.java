package local.hal.st32.android.akanetin;

import java.util.ArrayList;
import java.util.HashMap;

public interface SQL extends Common {

	final class SELECT {
		private String _tableName;
		private ArrayList<String> _columnList;
		private HashMap<String, Integer> _columnMap;
		private ArrayList<String> _whereList;
		private ArrayList<String> _joinList;
		{
			this._tableName = "";
			this._columnList = new ArrayList<String>();
			this._columnMap = new HashMap<String, Integer>();
			this._whereList = new ArrayList<String>();
			this._joinList = new ArrayList<String>();
		}

		/**
		 * テーブル名を取得してクラス内のString変数に格納
		 * @param tableName
         */
		public SELECT(String tableName) {
			// TODO 自動生成されたコンストラクター・スタブ
			this._tableName = tableName;
		}

		/**
		 * 列名を受け取り、列名と列番号を格納
		 * @param columnName
         */
		public void add(String columnName) {
			this._columnList.add(columnName);
			this._columnMap.put(columnName, this._columnList.size() - 1);
		}

		/**
		 * テーブル名と列名を取得し、「.」でつなげる
		 * @param tableName
		 * @param columnName
         */
		public void add(String tableName, String columnName) {
			this.add(tableName + COLON + columnName);
		}

		public void addWhere(WHERE where) {
			this.addWhere(where.getWhere());
		}

		public void addWhere(String where) {
			this._whereList.add(where);
		}

		public void addAND() {
			this._whereList.add(AND);
		}

		public void addOR() {
			this._whereList.add(OR);
		}

		public void addJoin(String join) {
			this._joinList.add(join);
		}

		public void addJoin(JOIN join) {
			this.addJoin(join.getJoin());
		}

		public String out() {
			StringBuffer sbSelect = new StringBuffer();
			sbSelect.append(SELECT + SPACE);
			int i = 0;
			for (String column : this._columnList) {
				if (i > 0)
					sbSelect.append(COMMA);
				sbSelect.append(column);
				i++;
			}
			sbSelect.append(SPACE + FROM + SPACE + this._tableName);
			if (this._joinList.isEmpty() == false) {
				for (String join : this._joinList) {
					sbSelect.append(SPACE + join);
				}
			}
			if (this._whereList.isEmpty() == false) {
				sbSelect.append(SPACE + WHERE);
				for (String where : this._whereList) {
					sbSelect.append(SPACE + where);
				}
			}
			sbSelect.append(SPACE + SEMICOLON);
			return sbSelect.toString();
		}
	}

	interface WHERE {
		String getWhere();
	}

	class WhereBase implements WHERE {
		private String _column;
		private String _strVal;
		private int _intVal;
		private String _conditions;
		{
			this._conditions = "";
			this._strVal = "";
		}

		public WhereBase(String column, String condition, String val) {
			// TODO 自動生成されたコンストラクター・スタブ
			this._column = column;
			this._conditions = condition;
			this._strVal = val;
		}

		public WhereBase(String column, String condition, int val) {
			// TODO 自動生成されたコンストラクター・スタブ
			this._column = column;
			this._conditions = condition;
			this._intVal = val;
		}
		@Override
		public String getWhere() {
			// TODO 自動生成されたメソッド・スタブ
			StringBuffer sbWhere = new StringBuffer();
			sbWhere.append(this._column + SPACE + this._conditions + SPACE);
			if (this._strVal.isEmpty() == true) {
				sbWhere.append(this._intVal);
			} else {
				sbWhere.append(SINGLE + this._strVal + SINGLE);
			}
			return sbWhere.toString();
		}
	}

	/**
	 * @author 浩生
	 * @param =
	 * @retunr WHERE_EQUALS
	 */
	final class EQUALS extends WhereBase {
		private static final String equal = "=";

		public EQUALS(String column, String val) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(column, equal, val);
		}

		public EQUALS(String column, int val) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(column, equal, val);
		}
	}

	final class BETWEEN implements WHERE{
		private String _column;
		private String _strVal1;
		private String _strVal2;
		private int _intVal1;
		private int _intVal2;
		{
			this._strVal1="";
			this._strVal2="";
		}
		public BETWEEN(String columnName,String val1,String val2) {
			// TODO 自動生成されたコンストラクター・スタブ
			this._column=columnName;
			this._strVal1=val1;
			this._strVal2=val2;
		}
		public BETWEEN(String columnName,int val1,int val2) {
			// TODO 自動生成されたコンストラクター・スタブ
			this._column=columnName;
			this._intVal1=val1;
			this._intVal2=val2;
		}
		@Override
		public String getWhere() {
			// TODO 自動生成されたメソッド・スタブ
			StringBuffer sbBetween=new StringBuffer();
			sbBetween.append(this._column+SPACE+BETWEEN+SPACE);
			if(this._strVal1.isEmpty()==false && this._strVal2.isEmpty()==false){
				sbBetween.append(SINGLE+this._strVal1+SINGLE+SPACE+AND+SPACE+SINGLE+this._strVal2+SINGLE);
			}else{
				sbBetween.append(this._intVal1+SPACE+AND+SPACE+this._intVal2);
			}
			return sbBetween.toString();
		}
	}
	/**
	 * @author 浩生
	 * @param >=
	 * @retunr WHERE_THAT_ALL
	 */
	final class THAT_ALL extends WhereBase {
		private static final String all = ">=";

		public THAT_ALL(String column, String val) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(column, all, val);
		}

		public THAT_ALL(String column, int val) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(column, all, val);
		}
	}

	/**
	 * @author 浩生
	 * @param >
	 * @retunr WHERE_GREATER
	 */
	final class GREATER extends WhereBase {
		private static final String greater = ">";

		public GREATER(String column, String val) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(column, greater, val);
		}

		public GREATER(String column, int val) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(column, greater, val);
		}
	}

	/**
	 * @author 浩生
	 * @param <=
	 * @retunr WHERE_LESS_THAN
	 */
	final class LESS_THAN extends WhereBase {
		private static final String less = "<=";

		public LESS_THAN(String column, String val) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(column, less, val);
		}

		public LESS_THAN(String column, int val) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(column, less, val);
		}
	}

	/**
	 * @author 浩生
	 * @param <
	 * @retunr WHERE_INSUFFCIENT
	 */
	final class INSUFFCIENT extends WhereBase {
		private static final String insu = "<";

		public INSUFFCIENT(String column, String val) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(column, insu, val);
		}

		public INSUFFCIENT(String column, int val) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(column, insu, val);
		}
	}

	interface JOIN {
		String getJoin();
	}

	class ON {
		protected String on1;
		protected String on2;
	}

	class JoinBase implements JOIN {
		private ArrayList<ON> _onList;
		private String _tableName1;
		private String _tableName2;
		protected String _condition;
		private static final String JOIN = "JOIN";
		private static final String ON = "ON";
		{
			this._tableName1 = "";
			this._onList = new ArrayList<SQL.ON>();
		}

		public JoinBase(String tableName1, String tableName2, String on1,
				String on2) {
			// TODO 自動生成されたコンストラクター・スタブ
			this._tableName1 = tableName1;
			this._tableName2 = tableName2;
			addON(on1, on2);
		}

		public void addON(String on1, String on2) {
			ON on = new ON();
			on.on1 = this._tableName1 + COLON + on1;
			on.on2 = this._tableName2 + COLON + on2;
			this._onList.add(on);
		}

		@Override
		public String getJoin() {
			// TODO 自動生成されたメソッド・スタブ
			StringBuffer sbJoin = new StringBuffer();
			sbJoin.append(this._condition + SPACE + JOIN);
			sbJoin.append(SPACE + this._tableName2 + SPACE + ON);
			int i = 0;
			for (ON on : this._onList) {
				if (i > 0) {
					sbJoin.append(SPACE + AND);
				}
				sbJoin.append(SPACE + on.on1 + EQUAL + on.on2);
				i++;
			}
			return sbJoin.toString();
		}
	}

	final class JOIN_ON extends JoinBase {
		{
			super._condition = "";
		}

		public JOIN_ON(String tableName1, String tableName2, String on1,
				String on2) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(tableName1, tableName2, on1, on2);
		}
	}

	final class INNER_JOIN_ON extends JoinBase {
		{
			super._condition = "INNER";
		}

		public INNER_JOIN_ON(String tableName1, String tableName2, String on1,
				String on2) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(tableName1, tableName2, on1, on2);
		}
	}

	final class OUTER_JOIN_ON extends JoinBase {
		{
			super._condition = "OUTER";
		}

		public OUTER_JOIN_ON(String tableName1, String tableName2, String on1,
				String on2) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(tableName1, tableName2, on1, on2);
		}
	}

	final class LEFT_OUETR_JOIN_ON extends JoinBase {
		{
			super._condition = "LEFT" + SPACE + "OUTER";
		}

		public LEFT_OUETR_JOIN_ON(String tableName1, String tableName2,
				String on1, String on2) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(tableName1, tableName2, on1, on2);
		}
	}

	interface FUNCTION {
		String getFunction();
	}

	class FunctionBase implements FUNCTION {
		private ArrayList<String> _columnList;
		protected String _condition;
		private String _as;
		{
			this._as = "";
		}

		public void addColumn(String column) {
			this._columnList.add(column);
		}

		public void addVal(String val) {
			this.addColumn(SINGLE + val + SINGLE);
		}

		protected void addAs(String as) {
			this._as = as;
		}

		@Override
		public String getFunction() {
			// TODO 自動生成されたメソッド・スタブ
			StringBuffer sbFunction = new StringBuffer();
			sbFunction.append(this._condition);
			sbFunction.append(SPACE + BRACKETS_START);
			int i = 0;
			for (String column : this._columnList) {
				if (i > 0)
					sbFunction.append(COMMA);
				sbFunction.append(column);
			}
			sbFunction.append(BRACKETS_END);
			if (this._as.isEmpty() != true) {
				sbFunction.append(SPACE + AS + SPACE + this._as);
			}
			return sbFunction.toString();
		}
	}

	final class SUM extends FunctionBase {
		private static final String sum = "SUM";
		{
			super._condition = sum;
		}

		public SUM(String column) {
			// TODO 自動生成されたコンストラクター・スタブ
			super.addColumn(column);
		}

		public SUM(String column, String as) {
			// TODO 自動生成されたコンストラクター・スタブ
			super.addColumn(column);
			super.addAs(as);
			;
		}
	}

	final class IFNULL extends FunctionBase {
		private static final String ifnull = "IFNULL";
		{
			super._condition = ifnull;
		}

		public IFNULL(String column, String nullVal) {
			// TODO 自動生成されたコンストラクター・スタブ
			super.addColumn(column);
			super.addVal(nullVal);
		}

		public IFNULL(String column, String nullVal, String as) {
			// TODO 自動生成されたコンストラクター・スタブ
			super.addColumn(column);
			super.addVal(nullVal);
			super.addAs(as);
		}
	}

	final class RIGHT_OUTER_JOIN_ON extends JoinBase {
		{
			super._condition = "RIGHT" + SPACE + "OUETR";
		}

		public RIGHT_OUTER_JOIN_ON(String tableName1, String tableName2,
				String on1, String on2) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(tableName1, tableName2, on1, on2);
		}
	}

	final class ColumnException extends Exception {
		protected String _columnName;

		public ColumnException(String columnName) {
			// TODO 自動生成されたコンストラクター・スタブ
			this._columnName = columnName;
		}
	}
}
