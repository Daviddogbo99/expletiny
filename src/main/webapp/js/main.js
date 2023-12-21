/**
 * URL Constants
 */

const getPet = "api/petition/v1/getPetitions";
const createPet = "api/petition/v1/CreatePetition";
const sign = "api/petition/v1/sign";
const createPetUnsafe = "api/petition/v1/unsafeCreatePet";
const signUnsafe = "api/petition/v1/unsafeSign";
const getSignedBy = "api/petition/v1/getSignedBy";
const topPets = "api/petition/v1/topPets";
const randomPet = "api/petition/v1/RandomPet";

/**
 * 
 */
/*
var Petition = {
    list: [],
    loadList: function() {
        return m.request({
            method: "GET",
            url: "_ah/api/petition/v1/getPetitions/"
        })
        .then(function(result) {
            Petition.list = result.items
            console.log("got:",result.items)
            // m.redraw(true) 
        })
    },
    save: function(UserEmail,petition) {
        console.log("saving...",Petition.current)
        return m.request({
            method: "GET",
            url: "_ah/api/petition/v1/unsafeCreatePet/"+UserEmail+"/"+petition
        })
        .then(function(result) {
            console.log("got:",result)
            Petition.loadList()
        })
    }
}
      
var PetitionView = {
    oninit: Petition.loadList,
    view: function() {
        return m('div', [
            m('div',{class:'subtitle'},"Top 100 Petitions"),
            m('table', {class:'table is-striped'},[
                m('tr', [
                    m('th', {width:"20px"}, "User Name"),
                    m('th', {width:"50px"}, "Petition"),
                ]),
                Petitions.list.map(function(item) {
                    return m("tr", [
                        m('td', m('label', item.properties.name)),
                        m('td', m('label', item.properties.Petitions)),
                    ])
                })
            ])
        ])
    }
}
      
      
let Body = {
    view: function() {
        return m('div', {class:'container'}, [
            m("h1", {class: 'title'}, 'TinyPet 2023'),
            m('div',{class: 'tile is-ancestor'},[
                m("div", {class: 'tile'}, m('div',{class:'tile is-child box'},m(PetitionView))),
            ])
        ])
    }
}
*/

/**
 * The main component that displays either my profile or the posts
 */
var MainView = {
    //The type of the current post ("welcome": posts; "profile": profile)
    type: "welcome",

    /**
     * The view function
     */
    view: function () {
        switch (this.type) {
            case "welcome":
                return PostView
                    .view();
            case "profile":
                return ProfileView
                    .view();
            case "apropos":
                return AproposView
                    .view();
        }
    },

    /**
     * The function that allows you to change views
     * @param {view} view The type of view
     */
    changeView: function (view) {
        if (User.isLoged()) {
            if (this.type !=
                view) {
                this.type = view
            }
            if (view ==
                "profile") {
                Post
                    .loadListPerso();
            } else {
                Post
                    .loadListRandom();
            }

        }
    }

}

/**
 * The component that manages the A propos view
 */
var AproposView = {
    view: function () {
        return [
            m("div", {
                "class": "police apropos"
            },
                [
                    m("div", {
                        "class": "about-section apropos"
                    },
                        [
                            m("h1",
                                "À propos de nous"
                            ),
                            m("p",
                                "L'équipe créatrice de cette excellente, incroyable, phénoménale, invraisemblable, fantastique, extraordinaire, abracadabrante, impressionnante, stupéfiante, rocambolesque, fantasmagorique application Web \"TinyGram\" est composée de 3 étudiants de Nantes Université. Qui sont ces membres hors du commun ?!?!?"
                            )
                        ]
                    ),
                    m("h2", {
                        "style": {
                            "text-align": "center"
                        },
                        class: "m-4"
                    },
                        "Notre équipe de choc"
                    ),
                    m("div", {
                        "class": "row apropos"
                    },
                        [
                            m("div", {
                                "class": "column apropos"
                            },
                                m("div", {
                                    "class": "card apropos"
                                },
                                    [
                                        m("img", {
                                            "src": "https://media.tenor.com/-DyBfnxjz3oAAAAS/kaamelott-arthur.gif",
                                            "class": "m-2"
                                        }),
                                        m("div", {
                                            "class": "container apropos"
                                        },
                                            [
                                                m("h2",
                                                    "Rodrigue Meunier"
                                                ),
                                                m("h5",
                                                    "Alias \"Rod4401\" ou \"Captaine Rillettes\""
                                                ),
                                                m("p", {
                                                    "class": "title apropos"
                                                },
                                                    "Student"
                                                ),
                                                m("p",
                                                    "Disposant d'un anglais proche de la perfection, Rodrigue Meunier a su investir corps et âme afin de garantir une belle interface visuelle de cette application. Ce manceau a la particularité d'être un joueur professionnel Minecraft et cela en parallèle de ses études. Il est tout à fait capable de construire un système en redstone qui scale et qui est efficace. Sa plus grande faiblesse se situe sur son téléphone, c'est Tiktok."
                                                ),
                                                m("p",
                                                    "rodrigue.meunier@etu.univ-nantes.fr"
                                                ),
                                                m("p",
                                                    m("button", {
                                                        "class": "button apropos"
                                                    },
                                                        "Contact"
                                                    )
                                                )
                                            ]
                                        )
                                    ]
                                )
                            ),
                            m("div", {
                                "class": "column apropos"
                            },
                                m("div", {
                                    "class": "card apropos"
                                },
                                    [
                                        m("img", {
                                            "src": "https://media.tenor.com/OpjEv-qkRIcAAAAC/kaamelott-perceval.gif",
                                            "class": "m-2"
                                        }),
                                        m("div", {
                                            "class": "container apropos"
                                        },
                                            [
                                                m("h2",
                                                    "Quentin Gomes Dos Reis"
                                                ),
                                                m("h5",
                                                    "Alias \"ThinkIsPossible\",\"Los Portos\""
                                                ),
                                                m("p", {
                                                    "class": "title apropos"
                                                },
                                                    "Student"
                                                ),
                                                m("p",
                                                    "Cet individu possède une photo de profil pour le moins... particulièrement angoissante. Cet intriguant personnage effectue depuis plus de 3 ans, la route en voiture ou en train tous les jours pour se rendre en cours à la fac depuis Clisson. Comment peut-on rester normal après ça...Il est aussi connu pour son implication dans le back à l'ombre du front et rappelle régulièrement à son camarade, le Capitaine Rillettes, que si une méthode du back ne fonctionne pas, c'est qu'il faut d'abord l'appeler correctement avant de blamer le back, bref affaire à suivre..."
                                                ),
                                                m("p",
                                                    "quentin.gomes-dos-reis@etu.univ-nantes.fr"
                                                ),
                                                m("p",
                                                    m("button", {
                                                        "class": "button apropos"
                                                    },
                                                        "Contact"
                                                    )
                                                )
                                            ]
                                        )
                                    ]
                                )
                            ),
                            m("div", {
                                "class": "column apropos"
                            },
                                m("div", {
                                    "class": "card apropos"
                                },
                                    [
                                        m("img", {
                                            "src": "https://media.tenor.com/k0b77ukWmA0AAAAM/kaamelott-merlin.gif",
                                            "class": "m-2"
                                        }),
                                        m("div", {
                                            "class": "container apropos"
                                        },
                                            [
                                                m("h2",
                                                    "Valentin Goubon"
                                                ),
                                                m("h5",
                                                    "Alias \"TinkyValou\""
                                                ),
                                                m("p", {
                                                    "class": "title apropos"
                                                },
                                                    "Student"
                                                ),
                                                m("p",
                                                    "Alors lui on se demande comment il est arrivé là. Plus efficace pour organiser un laser-game, ou bien pour devenir aussi puissant que Jotaro Kujo, Valentin a la particularité aussi de ne pas toujours venir en cours, pour des problèmes de réveil. Nous verrons bien si les quelques séances loupées ne lui seront pas indispensables dans sa réussite scolaire. Le délégué de la promotion ALMA va devoir s'accrocher !"
                                                ),
                                                m("p",
                                                    "valentin.goubon@etu.univ-nantes.fr"
                                                ),
                                                m("p",
                                                    m("button", {
                                                        "class": "button apropos"
                                                    },
                                                        "Contact"
                                                    )
                                                )
                                            ]
                                        )
                                    ]
                                )
                            )
                        ]
                    ),
                    m("div", {
                        "class": "about-section apropos"
                    },
                        [
                            m("h1",
                                "Lien vers notre projet"
                            ),
                            m("p",
                                [
                                    " Voici un lien vers notre projet Open Source, afin que vous puissiez admirer notre oeuvre. Si vous rencontrez le moindre souci sur notre application, ce qui est techniquement impossible, n'hésitez pas à contacter monsieur ",
                                    m("a", {
                                        "href": "Pascal.Molli@univ-nantes.fr"
                                    },
                                        "Pascal Molli"
                                    ),
                                    ". C'est notre directeur technique, il vous garantira une assistance hors du commun vous permettant de profiter de la meilleure expérience possible afin de passer une bonne procrastination. Github : ",
                                    m("a", {
                                        "href": "mailto:https://github.com/Rod4401/TinyGram"
                                    },
                                        "TinyGram"
                                    )
                                ]
                            ),
                            m("h1",
                                "Résultats sur la scalabilité de notre projet"
                            ),
                            m("div",
                                [
                                    m("h3", {"class":"mb-3 mt-3"}, "Courbe cumulée croissante de la latence"),
                                    m("img", {
                                        "src": "img/courbeCumuléeCroissante.png",
                                        "class": "mw-100",
                                    }),
                                    m("h3", {"class":"mb-3 mt-3"},"Courbe de la densité de la latence"),
                                    m("img", {
                                        "src": "img/courbeDensité.png",
                                        "class": "mw-100",
                                    }),
                                    m("h3", {"class":"mb-3 mt-3"},"Latences détaillées en fonction du pourcentage"),
                                    m("img", {"src": "img/LatenceDetaillée.png"}),
                                    m("p", {"class":"text-break"}, "D'après les rapports de Google. On peut voir que le cap des 500ms symbolique est dépassé au delà de 65% des requêtes. Étant donnée que nous n'avons pas executé nos requêtes sur des millions d'utilisateurs, la moindre latence (une personne ayant une connexion pour le moins douteuse) nous fait défaut. On ne peut pas conclure de rapport sur une aussi courte période mais on voit que pour un nombre assez restraint d'utilisateur, les requêtes sont déjà \"correctes\""),
                                ]
                            )
                        ]
                    )
                ]
            )
        ]
    }
}