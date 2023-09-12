package by.belyahovich.task1backendproject.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "file_data")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String file_path;
}
