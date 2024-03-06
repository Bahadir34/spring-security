package com.tpe.security;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component // daha sonradan JWTUtils'in methodları kullanılacağı için @Component ile anote edildi.
public class JWTUtils {
    /*
     * user ve password validasyonu yapılacak
     * JWT generate edilecek
     * JWT validasyonu yapılacak
     * */

    /*

        JWT token 3 parçadan oluşur
        1. header
        2. payload -> userla ilgili bilgiler taşınıyor.
        3. signiture -> token bu kısımda çalışır, imzalanır ve hash'lenir. tam bir güvenlik sağlar.
                        hash fonksiyonu ile şifrelenen password bir daha geri döndürülemez.
    */


    private long jwtExpressionTime = 86400000; // 1 günlük süredir.
    private String jwtSecretKey = "springsecurity";


    // 1. JWT token generate etmemiz gerekiyor.
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // login olmuş user'ı bize return eder. (authenticated)

        // login olan user'ın username'ini token içerisine koyacağız. Daha sonra bu token bile bize geldiğinde hangi user login olmuş
        // obje olarak bu user'a ulaşabileceğiz.

        return Jwts.builder() // jwt oluşturulmasını sağlar
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())// Date'e parametre girmezsek system.currentMillis() methodu çağırılır. Token'in oluşturulduğu anı ms olarak alır.
                .setExpiration(new Date(new Date().getTime() + jwtExpressionTime)) // tokenin sona ereceği süre
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey) // İmzalama algoritması belirlenir, bir scretkey verilir. Hashleme ile tek yönlü şifreleme yapılır.
                .compact(); // ayarları tamamlar ve token oluşturulur.
    }

    // 2. JWT tokeni doğrulamamız gerekiyor. (validasyon)

    public boolean validateToken(String token) {
        try {
            Jwts.parser() //jwt token 3 yapıdan oluşur. header,payload,signature. parser bu yapılara ayırır
                    .setSigningKey(jwtSecretKey) // tokenin karşılaştırılacağı anahtar kelime.
                    .parseClaimsJws(token); // cermiş olduğumuz tokenin doğrulamasını yapıyor. imzalar uyumlu mu diye kontrol ediyor.

            return true; // imzalar uyumlu ise true return edilir.
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return false; // eğer hata fırlarsa false return eder.
    }


    // 3. daha sonraki requestlerde requestin header'ı üzerinden token gönderilecek ve işlemler token üzerinden gerçekleşecek.
    public String getUsernameFromJwtToken(String token){
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token) // token doğrulanır ve doğulanmış token'in içerisindeki claims'lerini return eder.
                .getBody() // getBody bir claimsdir.
                .getSubject(); // body içerisindeki subjecti al. Bizim subjectimiz zaten username'imizdir.
    }
}
