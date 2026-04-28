import React, { useState, useEffect, useRef } from "react";
import { CalendarDays, Flame, Archive, Star } from "lucide-react";

export default function HomePage() {
  const [location, setLocation] = useState("");
  const [date, setDate] = useState("");
  const [nights, setNights] = useState(1);
  const [equipment, setEquipment] = useState("");

  // ---------------- SLIDER ----------------
  const CARD_WIDTH = 280;
  const CARD_GAP = 24;
  const SLIDE_STEP = CARD_WIDTH + CARD_GAP;

  const [reviewIndex, setReviewIndex] = useState(0);
  const sliderRef = useRef(null);

  const reviews = [
    { text: "Un séjour incroyable ! Camping propre et super bien organisé.", author: "Marie D.", rating: 5 },
    { text: "Réservation ultra facile, et le site était encore mieux que prévu !", author: "Jean P.", rating: 5 },
    { text: "Service impeccable, activités variées, un vrai plaisir en famille.", author: "Sophie L.", rating: 4 },
    { text: "Meilleure expérience de camping de ma vie. Je recommande !", author: "François B.", rating: 5 },
    { text: "Très bon accueil, personnel attentionné, je reviendrai !", author: "Julie T.", rating: 5 },
    { text: "Les enfants ont adoré, activités variées et ambiance parfaite.", author: "Martin R.", rating: 5 },
  ];

  // On duplique 3 fois → slider ultra fluide
  const loopedReviews = [...reviews, ...reviews, ...reviews];

  useEffect(() => {
    const interval = setInterval(() => {
      setReviewIndex((prev) => prev + 1);
    }, 3000);

    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    // Quand on dépasse la longueur du 1er set original :
    if (reviewIndex >= reviews.length) {
      // Reset immédiat SANS transition
      setTimeout(() => {
        setReviewIndex(0);
        if (sliderRef.current) {
          sliderRef.current.style.transition = "none";
        }
      }, 400);
    } else {
      // Transition normale
      if (sliderRef.current) {
        sliderRef.current.style.transition = "transform 0.7s ease-out";
      }
    }
  }, [reviewIndex]);

  return (
    <div className="min-h-screen bg-white text-gray-800">

      {/* HERO */}
      <section className="relative max-w-7xl mx-auto px-6 mt-10 grid md:grid-cols-2 gap-10 items-start">
        
        {/* Texte */}
        <div className="relative z-10">
          <h1 className="text-4xl md:text-5xl font-bold leading-tight">
            Trouvez Votre<br />Prochain Camping
          </h1>
          <p className="mt-4 text-lg text-gray-700">
            Recherchez et réservez des emplacements de camping idylliques.
          </p>
        </div>

        {/* Image */}
        <div className="relative w-full h-[300px] md:h-[350px] z-0">
          <img
            src="/backgrounds/hero.png"
            alt="Camping Hero"
            className="rounded-lg shadow-lg w-full h-full object-cover"
          />
        </div>

        {/* Search box */}
        <div className="absolute left-0 top-[300px] z-20 w-[95%] max-w-6xl">
          <div className="bg-white shadow-xl shadow-black/10 rounded-2xl px-8 py-6">

            <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
              <div>
                <label className="font-semibold">Location*</label>
                <input
                  type="text"
                  placeholder="Ville ou région"
                  className="w-full border rounded-lg px-4 py-2 mt-1"
                  value={location}
                  onChange={(e) => setLocation(e.target.value)}
                />
              </div>

              <div>
                <label className="font-semibold">Date</label>
                <input
                  type="date"
                  className="w-full border rounded-lg px-4 py-2 mt-1"
                  value={date}
                  onChange={(e) => setDate(e.target.value)}
                />
              </div>

              <div>
                <label className="font-semibold">Nbr de nuits</label>
                <input
                  type="number"
                  min="1"
                  className="w-full border rounded-lg px-4 py-2 mt-1"
                  value={nights}
                  onChange={(e) => setNights(e.target.value)}
                />
              </div>

              <div>
                <label className="font-semibold">Équipement</label>
                <select
                  className="w-full border rounded-lg px-4 py-2 mt-1"
                  value={equipment}
                  onChange={(e) => setEquipment(e.target.value)}
                >
                  <option value="">Choisir...</option>
                  <option value="vr">VR</option>
                  <option value="tente">Tente</option>
                  <option value="vr-tente">VR + Tente</option>
                </select>
              </div>
            </div>

            <p className="text-red-600 text-sm mt-3">
              *Cette recherche couvre un rayon d’environ 50 km autour de votre position actuelle.
            </p>

            <div className="text-center mt-6">
              <button className="bg-orange-600 text-white text-lg font-semibold px-10 py-3 rounded-lg shadow hover:bg-orange-700">
                Rechercher
              </button>
            </div>
          </div>
        </div>
      </section>

      {/* SERVICES */}
      <section className="max-w-7xl mx-auto px-6 mt-56">
        <h2 className="text-3xl font-bold">Nos Services</h2>

        <div className="grid md:grid-cols-3 gap-6 mt-8">
          <div className="p-6 border rounded-xl bg-white shadow-sm">
            <h3 className="text-xl font-semibold flex items-center gap-3">
              <CalendarDays className="w-7 h-7 text-orange-600" />
              Réservation en ligne
            </h3>
            <p className="mt-2 text-gray-600">Réservez facilement en quelques clics.</p>
          </div>

          <div className="p-6 border rounded-xl bg-white shadow-sm">
            <h3 className="text-xl font-semibold flex items-center gap-3">
              <Flame className="w-7 h-7 text-orange-600" />
              Activités de plein air
            </h3>
            <p className="mt-2 text-gray-600">Randonnée, kayak, vélo et bien plus encore.</p>
          </div>

          <div className="p-6 border rounded-xl bg-white shadow-sm">
            <h3 className="text-xl font-semibold flex items-center gap-3">
              <Archive className="w-7 h-7 text-orange-600" />
              Gestion des réservations
            </h3>
            <p className="mt-2 text-gray-600">Consultez, modifiez et gérez vos séjours facilement.</p>
          </div>
        </div>
      </section>

      {/* CLIENT REVIEWS */}
      <section className="max-w-7xl mx-auto px-6 mt-20 overflow-hidden">
        <h2 className="text-3xl font-bold mb-6">Ce Que Disent Nos Clients</h2>

        <div className="relative overflow-hidden w-full">
          <div
            ref={sliderRef}
            className="flex gap-6"
            style={{
              transform: `translateX(-${reviewIndex * SLIDE_STEP}px)`,
            }}
          >
            {loopedReviews.map((review, i) => (
              <div
                key={i}
                className="border rounded-xl px-6 py-4 bg-white shadow-sm shrink-0"
                style={{ width: `${CARD_WIDTH}px` }}
              >
                <div className="flex gap-1 text-orange-500 mb-2">
                  {[...Array(review.rating)].map((_, j) => (
                    <Star key={j} size={18} fill="orange" stroke="orange" />
                  ))}
                </div>

                <p className="text-gray-600 italic">“{review.text}”</p>
                <p className="text-sm text-gray-500 mt-2">— {review.author}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="mt-20 bg-orange-50 py-16">
        <div className="max-w-3xl mx-auto px-6 text-center">
          <h2 className="text-3xl font-bold">Prêt à Explorer la Nature ?</h2>
          <button className="mt-6 bg-orange-600 text-white text-lg font-semibold px-10 py-3 rounded-lg shadow hover:bg-orange-700">
            Réservez Maintenant
          </button>
        </div>
      </section>

    </div>
  );
}
