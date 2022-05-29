package com.application.DaoLayer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name="photos")
public class Photo {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    private String id;
    @Column(name="fileName")
    private String fileName;
    @Column(name="fileType")
    private String fileType;
    @Column(name="data")
    @Lob
    private byte[] data;

    public Photo(String fileName, String fileType, byte[] data){
        this.fileName=fileName;
        this.fileType=fileType;
        this.data=data;
    }
}
