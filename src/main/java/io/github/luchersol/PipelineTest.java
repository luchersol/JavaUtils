package io.github.luchersol;

public class PipelineTest {

    // -----------------------------
    // Clases de ejemplo
    // -----------------------------
    static class Persona {
        private final InnerPerson inner;
        private final Long id;

        public Persona(InnerPerson inner, Long id) {
            this.inner = inner;
            this.id = id;
        }

        public InnerPerson getInnerPerson() {
            return inner;
        }

        public Persona emptyCopy() {
            return new Persona(null, null);
        }

        @Override
        public String toString() {
            return "Persona{inner=" + inner + ", id=" + id + "}";
        }
    }

    static class InnerPerson {
        private final Long id;

        public InnerPerson(Long id) { this.id = id; }

        public Long getId() { return id; }

        public InnerPerson emptyCopy() { return new InnerPerson(null); }

        @Override
        public String toString() {
            return "InnerPerson{id=" + id + "}";
        }
    }


    // -----------------------------
    // Main de prueba
    // -----------------------------
    public static void main(String[] args) {

        Persona persona1 = new Persona(new InnerPerson(42L), 1L);
        Persona persona2 = new Persona(null, 2L);

        // Crear pipeline reusable
        Pipeline<Persona, Long> pipeline = Pipeline.<Persona>empty()
                .addOrElse(Persona::getInnerPerson, () -> new InnerPerson(null))
                .addSwitch(InnerPerson::getId, id -> id != null && id > 0,() -> -1L);

        // Aplicar pipeline a varios objetos
        Long id1 = pipeline.apply(persona1); // 42
        Long id2 = pipeline.apply(persona2); // -1 (fallback)

        System.out.println("Resultado persona1: " + id1);
        System.out.println("Resultado persona2: " + id2);
    }
}

