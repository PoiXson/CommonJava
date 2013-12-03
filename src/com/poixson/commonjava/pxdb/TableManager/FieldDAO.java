package com.poixson.commonjava.pxdb.TableManager;

import com.poixson.commonjava.Utils.utilsSan;
import com.poixson.commonjava.pxdb.dbQuery;


public class FieldDAO {

	public final String type;
	public final String fieldName;
	public final String size;
	public final String def;
	public final boolean nullable;


	// field dao
	public FieldDAO(String fieldType, String fieldName, String size, String def, boolean nullable) {
		this.type = fieldType;
		this.fieldName = utilsSan.AlphaNumSafe(fieldName);
		this.size = size;
		this.def  = def;
		this.nullable = nullable;
	}


	// id field
	public static String sqlIdField(String fieldName) {
		return (new StringBuilder())
			.append("`")
			.append(dbQuery.san(fieldName))
			.append("` int(11) NOT NULL AUTO_INCREMENT, PRIMARY KEY (`")
			.append(dbQuery.san(fieldName))
			.append("`)")
		.toString();
	}
	// `name` type(size) NULL DEFAULT NULL
	public String sqlField() {
		StringBuilder sql = new StringBuilder();
		sql.append("`").append(fieldName).append("` ");
		switch(type.toLowerCase()) {
		case "s":
		case "str":
		case "string":
			sql.append("VARCHAR").append("(").append(size).append(")");
			break;
		case "i":
		case "int":
		case "integer":
			sql.append("INT").append("(").append(size).append(")");
			break;
		case "dec":
		case "decimal":
			sql.append("DECIMAL").append("(").append(size).append(")");
			break;
		case "d":
		case "dbl":
		case "double":
			sql.append("DOUBLE").append("(").append(size).append(")");
			break;
		case "f":
		case "flt":
		case "float":
			sql.append("FLOAT").append("(").append(size).append(")");
			break;
		case "l":
		case "lng":
		case "long":
			sql.append("LONG").append("(").append(size).append(")");
			break;
		case "b":
		case "bool":
		case "boolean":
			sql.append("TINYINT(1)");
			break;
		case "t":
		case "txt":
		case "text":
			sql.append("TEXT");
		default:
			break;
		}
		return sql.toString();
	}


}