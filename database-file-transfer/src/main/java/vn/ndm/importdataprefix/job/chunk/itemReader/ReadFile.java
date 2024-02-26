package vn.ndm.importdataprefix.job.chunk.itemReader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.File;

public class ReadFile extends MultiResourceItemReader<File> {

    public MultiResourceItemReader<Object> multiResourceItemReader(@Value("#{jobParameters['inputDirectory']}") String inputDirectory) {

        return null;
    }
}
