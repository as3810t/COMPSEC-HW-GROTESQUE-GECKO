package hu.grotesque_gecko.caffstore.user.models;

import hu.grotesque_gecko.caffstore.caff.models.CAFF;
import hu.grotesque_gecko.caffstore.caff.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name="users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private String id;

    @NotEmpty
    @Column(nullable = false)
    private String username;

    @NotEmpty
    @Column(nullable = false)
    private String email;

    @Column
    private String password;

    @Column(nullable = false)
    private boolean isAdmin;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date credentialValidityDate;

    @OneToMany(
        mappedBy = "owner",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<CAFF> caffs = new ArrayList<>();

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.PERSIST,
        orphanRemoval = false
    )
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<TemporaryPassword> temporaryPasswords = new ArrayList<>();

    public List<String> getRoles() {
        if(isAdmin) {
            return Collections.singletonList("ROLE_ADMIN");
        }
        else {
            return Collections.singletonList("ROLE_USER");
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User )) return false;
        return id != null && id.equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
