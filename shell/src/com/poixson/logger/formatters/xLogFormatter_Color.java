package com.poixson.logger.formatters;

import com.poixson.logger.xLevel;
import com.poixson.logger.xLogRecord;
import com.poixson.utils.StringUtils;


public class xLogFormatter_Color extends xLogFormatter {



	public xLogFormatter_Color() {
	}



	@Override
	public String formatMsg(final xLogRecord record, final int lineIndex) {
		// [[ title ]]
		if (xLevel.TITLE.equals(record.level)) {
			return
				this.genTitle(
					record,
					lineIndex,
					" @|FG_MAGENTA [[|@ @|FG_CYAN ",
					"|@ @|FG_MAGENTA ]]|@ "
				);
		}
		// timestamp [level] [crumbs]
		return
			StringUtils.MergeStrings(
				' ',
				// timestamp
				this.genTimestamp(
					record,
					"D yyyy-MM-dd HH:mm:ss",
					"@|FG_WHITE ",
					"|@"
				),
				// [level]
				this.genLevel(record),
				// [crumbs]
				this.genCrumbs(record),
				// message
				this.genMessage(record, lineIndex)
			);
	}



	// ------------------------------------------------------------------------------- //
	// generate parts



	// [level]
	protected String genLevel(final xLogRecord record) {
		return (new StringBuilder())
			.append("@|FG_BLACK,BOLD [|@@|")
			.append( this.getLevelColor(record.level) )
			.append(' ')
			.append( StringUtils.PadCenter(7, record.getLevelStr(), ' ') )
			.append("|@@|FG_BLACK,BOLD ]|@")
			.toString();
	}
	protected String getLevelColor(final xLevel level) {
		// all, finest, finer, fine
		if (level.isLoggable(xLevel.FINE))
			return "FG_BLACK,BOLD";
		// info
		if (level.isLoggable(xLevel.INFO))
			return "FG_CYAN";
		// warning
		if (level.isLoggable(xLevel.WARNING))
			return "FG_RED";
		// severe
		if (level.isLoggable(xLevel.SEVERE))
			return "FG_RED,BOLD";
		// fatal
		if (level.isLoggable(xLevel.FATAL))
			return "FG_RED,BOLD,UNDERLINE";
		// stdout
		if (level.isLoggable(xLevel.STDOUT))
			return "FG_GREEN";
		// stderr
		if (level.isLoggable(xLevel.STDERR))
			return "FG_YELLOW";
		// off
		return "FG_BLACK,BOLD";
	}



	// crumbs
	protected String genCrumbs(final xLogRecord record) {
		return (new StringBuilder())
			.append("@|FG_BLACK,BOLD ")
			.append( super.genCrumbs(record, "[", "] [", "]") )
			.append("|@")
			.toString();
	}



}