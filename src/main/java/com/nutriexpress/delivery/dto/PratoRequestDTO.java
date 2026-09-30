package com.nutriexpress.delivery.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;


public record PratoRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
        String nome,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        String descricao,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        @Digits(integer = 8, fraction = 2,
                message = "O valor deve ter no máximo 8 dígitos inteiros e 2 casas decimais")
        BigDecimal valor,

        @NotBlank(message = "A categoria é obrigatória")
        @Pattern(regexp = "vegano|low carb|fitness|sobremesa saudável",
                message = "A categoria deve ser: vegano, low carb, fitness ou sobremesa saudável")
        String categoria,

        @NotNull(message = "As calorias são obrigatórias")
        @PositiveOrZero(message = "As calorias não podem ser negativas")
        Integer calorias,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Double quantidade,

        @NotBlank(message = "A unidade de medida é obrigatória")
        @Pattern(regexp = "g|ml", message = "A unidade de medida deve ser g ou ml")
        String unidadeMedida
) {
}
