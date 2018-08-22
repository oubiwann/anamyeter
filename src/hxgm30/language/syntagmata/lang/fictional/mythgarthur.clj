(ns hxgm30.language.syntagmata.lang.fictional.mythgarthur
  "To use the languages defined here, you can do the following:
  ```
    (require '[hxgm30.language.syntagmata.lang.core :as lang]
             '[hxgm30.language.syntagmata.lang.fictional.mythgarthur :as myth)
    (lang/paragraph mythgarthur/elvish)
  ```
  Which will give output along the lines of the following:
  ```
    Réðiglǣð hápugalalju. Anntilligi bhakk. Sírr sīn
    lérð tar fögluttúw hangu. Wu dùlcestitna vins marga
    mǣt bigufitir kö daforðinn sǣn lǣðgaðatti. Rol.
    Fytuðran. Kongr merð mi þallmirdunn all hiða daur.
  ```")

(def orcish
  {:arabic 9
   :afrikaans 8
   :chinese 5
   :german 4
   :oldnorse 1})

(def elvish
  {:oldnorse 5
   :oldenglish 2
   :gaelic 1})

(def human
  {:sanskrit 6
   :english 6
   :oldnorse 2
   :oldenglish 1})

(def dwarvish
  {:gaelic 5
   :scots 5
   :hebrew 5
   :german 1})
