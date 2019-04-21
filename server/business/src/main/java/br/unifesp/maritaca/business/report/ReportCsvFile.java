package br.unifesp.maritaca.business.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.business.answer.dto.ZipFileDTO;
import br.unifesp.maritaca.business.base.dto.AbstractBusiness;
import br.unifesp.maritaca.business.enums.ReportItemOpType;
import br.unifesp.maritaca.business.report.dto.RepData;
import br.unifesp.maritaca.business.report.dto.RepMap;
import br.unifesp.maritaca.business.report.dto.RepReport;

@Service("reportCsvFile")
public class ReportCsvFile extends AbstractBusiness {
	
	private static Logger logger = Logger.getLogger(ReportCsvFile.class);

	public ZipFileDTO buildCsvFile(RepData rd){
		ZipFileDTO zipFileDTO = new ZipFileDTO();
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Writer w = new OutputStreamWriter(baos);
			w.append("Report Title");
			w.append("\n");
			w.append(rd.getTitle());
			w.append("\n");
			w.append("Title");
			w.append(",");
			w.append("Type");
			w.append(",");
			w.append("Operation");
			w.append(",");
			w.append("Result");
			w.append("\n");
			
			for(RepReport r : rd.getLstReport()){
				w.append(r.getTitle());
				w.append(",");
				w.append(r.getType());
				w.append(",");
				w.append(ReportItemOpType.getOpDescription(r.getOperation()));
				w.append(",");
				if(r.getSimple() != null){
					w.append(r.getSimple());
				}
				else if(r.getMap() != null){
					for(RepMap m : r.getMap()){
						w.append(m.getValue());
						w.append(":");
						w.append(m.getKey());
						w.append(";");
					}
				}
				else{
					w.append("");
				}
				w.append("\n");
			}
			w.flush();
			w.close();
			baos.flush();
			baos.close();
			if("".equals(rd.getTitle()))
				zipFileDTO.setFilename("report");
			else
				zipFileDTO.setFilename(rd.getTitle());
			
        	zipFileDTO.setData(baos);
		} catch (IOException e) {
			zipFileDTO.setMessage(getText("error_unexpected"));
			logger.error("buildCsvFile: " + e.getMessage());
		}
		return zipFileDTO;
	}
}