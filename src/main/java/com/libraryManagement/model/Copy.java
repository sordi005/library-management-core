package com.libraryManagement.model;

import com.libraryManagement.enums.CopyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "copies")

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SuperBuilder
public  class Copy extends BaseEntity {

    @EqualsAndHashCode.Include
    @NotBlank
    @Column(name = "copy_number" ,nullable = false ,unique = true)
    private String copyNumber;

    @NotNull
    @Column(name = "copy_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CopyStatus status;

    @ManyToOne( fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publication_id", nullable = false)
    private Publication publication;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "copy")
    private List<LoanLine> loanLines  = new ArrayList<>();

    /**
     * Agrega una línea de préstamo a esta copia.
     * Mantiene la relación bidireccional y evita duplicados.
     */
    public void addLoanLine(LoanLine loanLine) {
        if (loanLines == null || loanLines.contains(loanLine))return;
        this.loanLines.add(loanLine);
        loanLine.setCopy(this);
    }
}
