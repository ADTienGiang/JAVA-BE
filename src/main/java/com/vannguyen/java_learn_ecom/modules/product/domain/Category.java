package com.vannguyen.java_learn_ecom.modules.product.domain;

public class Category {

    private final Long id;
    private String name;
    private String slug;
    private boolean active;

    public Category(Long id, String name, String slug) {
        validateName(name);
        validateSlug(slug);

        this.id = id;
        this.name = name.trim();
        this.slug = slug.trim().toLowerCase();
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public boolean isActive() {
        return active;
    }

    public void rename(String name, String slug) {
        validateName(name);
        validateSlug(slug);

        this.name = name.trim();
        this.slug = slug.trim().toLowerCase();
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    private void validateName(String name) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("Category name must not be blank");
        }
    }

    private void validateSlug(String slug) {
        if (isBlank(slug)) {
            throw new IllegalArgumentException("Category slug must not be blank");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}