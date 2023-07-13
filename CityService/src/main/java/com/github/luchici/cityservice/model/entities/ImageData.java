package com.github.luchici.cityservice.model.entities;

import com.github.luchici.cityservice.exception.ApiException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@NoArgsConstructor
@Entity
@Table(name = "IMAGES")
public class ImageData {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    @Column(name="image_id")
    private Long id;
    private String name;
    @Lob
    @Column(name = "image_data", length = 1000)
    private byte[] image;
    @ManyToOne(cascade = {PERSIST,MERGE,REFRESH})
    @JoinColumn(name = "id")
    private City city;


    public ImageData(MultipartFile file) {
        this.name = file.getOriginalFilename();
        try {
            this.image = file.getBytes();
        } catch (IOException e) {
            throw new ApiException("Something is wrong with the upload image", e);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, image);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageData otherImage = (ImageData) o;
        return Objects.equals(name, otherImage.name) &&
                Arrays.equals(image, otherImage.image);
    }
}
