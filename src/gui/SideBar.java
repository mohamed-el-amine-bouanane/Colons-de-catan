package gui;

import game.Jeu;
import game.GameRunner;
import game.Joeur;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;


import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import board.CarteD;
import board.Localisation;
import board.Plateau;
import board.Tuile;
import lib.GraphPaperLayout;

public class SideBar extends JPanel {

	private ListC rollPanel 			= new ListC();
	private ListC mainPanel				= new ListC();
	private ListC buyPanel				= new ListC();
	private ListC tradePanel			= new ListC();
	private ListC choosePlayerPanel		= new ListC();
	private ListC errorPanel			= new ListC();
	private ListC devPanel				= new ListC();
	private ListC resPanel				= new ListC();
	private String resSelection;
	private boolean done = false;
	private ListC stealPanel			= new ListC();
	private ListC placePanel			= new ListC();

	private ListC setupPanel			= new ListC();
	private ListC inputResourcesPanel	= new ListC();
	private ArrayList<String> inputResources	= new ArrayList<String>();
	private ArrayList<String> offerResources	= new ArrayList<String>();
	private ArrayList<String> sellResources		= new ArrayList<String>();
	private Joeur tradeChoice;
	//private final ExecutorService pool;
	private boolean IRPdone = true;
	private boolean secondRound = false;
	private int count = 0;

	private KComponent currentPlayerBox;
	private final FenetreJeu display;
	private final Font font = new Font("Arial", 1, 16);
	private int flag = 0;
	// For tracking where we are in turn; 0 = main panel or roll, 1 = trade panel, 2 = buy panel, 3 = dev card panel

	public final static int INTERVAL = 50;
	private Timer timer;

	public SideBar(final FenetreJeu display) {
		//pool = Executors.newSingleThreadExecutor();

		this.display = display;
		this.setLayout(new GraphPaperLayout(new Dimension(14,24)));

		// Current player title (always in sidebar)
		//-------------------------------------------------------------------

		currentPlayerBox = new KComponent(new JLabel(""), new Rectangle(2,0,10,1));
		currentPlayerBox.getComponent().setFont(font);
		currentPlayerBox.getComponent().setForeground(Color.WHITE);
		setCurrentPlayer(GameRunner.getCurrentPlayer());
		add(currentPlayerBox.getComponent(), currentPlayerBox.getRectangle());

		// Roll panel:
		//-------------------------------------------------------------------

		JButton roll = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu g = display.getP().getGame();

				if (g.over()) {
					GameRunner.setWinner(g.winningPlayer());

					winPanel();
				}

				int roll = g.roll(GameRunner.getCurrentPlayer());

				if (roll != 7) {
					mainPanel();
				}
				else {

					if (GameRunner.getNumbPlayers() == 3) {
						int remove = 0;
						if (GameRunner.getPlayer(0).getTotalResources() > 7)
							remove = GameRunner.getPlayer(0).getTotalResources() / 2;
						inputResourcesPanel(remove, GameRunner.getPlayer(0), "Remove " + remove + " resources", false);
						timer = new Timer(INTERVAL,
								new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (IRPdone) {
									timer.stop();
									GameRunner.getPlayer(0).removeResources(inputResources);
									int remove = 0;
									if (GameRunner.getPlayer(1).getTotalResources() > 7)
										remove = GameRunner.getPlayer(1).getTotalResources() / 2;
									inputResourcesPanel(remove, GameRunner.getPlayer(1), "Remove " + remove + " resources", false);
									timer = new Timer(INTERVAL,
											new ActionListener() {
										public void actionPerformed(ActionEvent evt) {
											if (IRPdone) {
												timer.stop();
												GameRunner.getPlayer(1).removeResources(inputResources);
												int remove = 0;
												if (GameRunner.getPlayer(2).getTotalResources() > 7)
													remove = GameRunner.getPlayer(2).getTotalResources() / 2;
												inputResourcesPanel(remove, GameRunner.getPlayer(2), "Remove " + remove + " resources", false);
												timer = new Timer(INTERVAL,
														new ActionListener() {
													public void actionPerformed(ActionEvent evt) {
														if (IRPdone) {
															timer.stop();
															GameRunner.getPlayer(2).removeResources(inputResources);
															display.getP().placeRobber();
															placePanel("Move the robber...");
															timer = new Timer(INTERVAL,
																	new ActionListener() {
																public void actionPerformed(ActionEvent evt) {
																	if(display.getP().getState() == 1){
																	}
																	else {
																		timer.stop();
																		stealPanel();
																	}
																}
															});
															timer.start();
														}
													}
												});
												timer.start();
											}
										}
									});
									timer.start();
								}
							}
						});
						timer.start();
					}
					else {
						//System.out.println("3");
						int remove = 0;
						if (GameRunner.getPlayer(0).getTotalResources() > 7)
							remove = GameRunner.getPlayer(0).getTotalResources() / 2;
						inputResourcesPanel(remove, GameRunner.getPlayer(0), "Remove " + remove + " resources", false);
						timer = new Timer(INTERVAL,
								new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (IRPdone) {
									timer.stop();
									GameRunner.getPlayer(0).removeResources(inputResources);
									int remove = 0;
									if (GameRunner.getPlayer(1).getTotalResources() > 7)
										remove = GameRunner.getPlayer(1).getTotalResources() / 2;
									inputResourcesPanel(remove, GameRunner.getPlayer(1), "Remove " + remove + " resources", false);
									timer = new Timer(INTERVAL,
											new ActionListener() {
										public void actionPerformed(ActionEvent evt) {
											if (IRPdone) {
												timer.stop();
												GameRunner.getPlayer(1).removeResources(inputResources);
												int remove = 0;
												if (GameRunner.getPlayer(2).getTotalResources() > 7)
													remove = GameRunner.getPlayer(2).getTotalResources() / 2;
												inputResourcesPanel(remove, GameRunner.getPlayer(2), "Remove " + remove + " resources", false);
												timer = new Timer(INTERVAL,
														new ActionListener() {
													public void actionPerformed(ActionEvent evt) {
														if (IRPdone) {
															timer.stop();
															GameRunner.getPlayer(2).removeResources(inputResources);
															int remove = 0;
															if (GameRunner.getPlayer(3).getTotalResources() > 7)
																remove = GameRunner.getPlayer(3).getTotalResources() / 2;
															inputResourcesPanel(remove, GameRunner.getPlayer(3), "Remove " + remove + " resources", false);
															timer = new Timer(INTERVAL,
																	new ActionListener() {
																public void actionPerformed(ActionEvent evt) {
																	if(IRPdone){
																		timer.stop();
																		GameRunner.getPlayer(3).removeResources(inputResources);
																		display.getP().placeRobber();
																		placePanel("Move the robber...");
																		timer = new Timer(INTERVAL,
																				new ActionListener() {
																			public void actionPerformed(ActionEvent evt) {
																				if(display.getP().getState() == 1){
																				}
																				else {
																					timer.stop();
																					stealPanel();
																				}
																			}
																		});
																		timer.start();
																	}
																}
															});
															timer.start();
														}
													}
												});
												timer.start();
											}
										}
									});
									timer.start();
								}
							}
						});
						timer.start();
					}
				}

				JLabel rollNumb = new JLabel("Roll value: " + roll);
				rollNumb.setFont(font);
				add(rollNumb, new Rectangle(2,2,10,1));
				repaint();
				validate();
			}
		});
		roll.setText("roll the dice");
		rollPanel.add(new KComponent(roll, new Rectangle(3,5,8,3)));

		// Main panel:
		//-------------------------------------------------------------------

		JButton buy = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				buyPanel();
			}
		});
		buy.setText("buy");
		mainPanel.add(new KComponent(buy, new Rectangle(3,5,8,3)));

		JButton trade = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				tradePanel();
			}
		});
		trade.setText("trade");
		mainPanel.add(new KComponent(trade, new Rectangle(3,9,8,3)));

		JButton dev = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				devPanel();
			}
		});
		dev.setText("play development card");
		mainPanel.add(new KComponent(dev, new Rectangle(3,13,8,3)));

		JButton endTurn = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu g = display.getP().getGame();
				
				// Check for largest army
				if (GameRunner.getCurrentPlayer().getNumbChevaliers() >= 3) {

					int currMax = GameRunner.getCurrentPlayer().getNumbChevaliers();
					Joeur largestArmy = GameRunner.getCurrentPlayer();
					Joeur oldLargestArmy = null;
					
					for (int i = 0; i < GameRunner.getNumbPlayers(); i++) {
						Joeur p = GameRunner.getPlayer(i);

						if (p.hasLargestArmy()) {
							oldLargestArmy = p;
						}

						if (p.getNumbChevaliers() > currMax) {
							largestArmy = p;
							currMax = p.getNumbChevaliers();
						}
					}
					
					if (oldLargestArmy != null && oldLargestArmy != largestArmy) {
						oldLargestArmy.setHasLargestArmy(false);
					}
					
					largestArmy.setHasLargestArmy(true);
				}

				if (g.over()) {
					GameRunner.setWinner(g.winningPlayer());

					winPanel();
				}

				GameRunner.nextPlayer();
				rollPanel();
			}
		});
		endTurn.setText("end your turn");
		mainPanel.add(new KComponent(endTurn, new Rectangle (3,18,8,3)));

		// Trade panel:
		//-------------------------------------------------------------------

		JLabel tradeText = new JLabel("Trade with...");
		tradePanel.add(new KComponent(tradeText, new Rectangle(4,4,6,2)));

		// Trade with players
		JButton tradePlayer = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				choosePlayerPanel();
			}
		});
		tradePlayer.setText("a player");
		tradePanel.add(new KComponent(tradePlayer, new Rectangle(1,6,6,2)));

		// Trade with stock
		JButton tradeStock = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				resPanel();
				timer = new Timer(INTERVAL,
						new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						if(done){
							timer.stop();
							done = false;
							final String res = resSelection;
							inputResourcesPanel(-1, GameRunner.getCurrentPlayer(), "Sell resources", false);
							timer = new Timer(INTERVAL,
									new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									if(IRPdone){
										timer.stop();
										display.getP().getGame().npcTrade(GameRunner.getCurrentPlayer(), res, inputResources);
										mainPanel();
									}
								}
							});
							timer.start();
						}
					}
				});
				timer.start();
			}
		});
		tradeStock.setText("the stock");
		tradePanel.add(new KComponent(tradeStock, new Rectangle(7,6,6,2)));

		// Return to main panel
		JButton returnMain = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				mainPanel();
			}
		});
		returnMain.setText("return to main panel");
		tradePanel.add(new KComponent(returnMain, new Rectangle(3,11,8,2)));

		// Buy panel:
		//-------------------------------------------------------------------

		JLabel buyText = new JLabel("Buy a...");
		buyPanel.add(new KComponent(buyText, new Rectangle(4,4,6,2)));

		// Buy settlement
		JButton buySettlement = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu g = display.getP().getGame();

				int bought = g.buySettlement(GameRunner.getCurrentPlayer());

				if (bought == 0) {
					display.getP().placeSettlement(1);
					placePanel("Place a settlement...");
					timer = new Timer(INTERVAL,
							new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if(display.getP().getState() == 2){

							}
							else {
								buyPanel();
								timer.stop();
							}
						}
					});
					timer.start();
				}
				else if (bought == 1) {
					errorPanel("Insufficient resources!");
				}
				else if (bought == 2) {
					errorPanel("Structure capacity reached!");
				}
			}
		});
		buySettlement.setText("settlement");
		buyPanel.add(new KComponent(buySettlement, new Rectangle(1,6,6,2)));

		// Buy city
		JButton buyCity = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu g = display.getP().getGame();

				int bought = g.buyCity(GameRunner.getCurrentPlayer());

				if (bought == 0) {
					display.getP().placeCity(1);
					placePanel("Select a settlement...");
					timer = new Timer(INTERVAL,
							new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if(display.getP().getState() == 4){

							}
							else {
								buyPanel();
								timer.stop();
							}
						}
					});
					timer.start();
				}
				else if (bought == 1) {
					errorPanel("Insufficient resources!");
				}
				else if (bought == 2) {
					errorPanel("Structure capacity reached!");
				}
			}
		});
		buyCity.setText("city");
		buyPanel.add(new KComponent(buyCity, new Rectangle(7,6,6,2)));

		// Buy road
		JButton buyRoad = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu g = display.getP().getGame();

				int bought = g.buyRoad(GameRunner.getCurrentPlayer());

				if (bought == 0) {
					display.getP().placeRoad(1);
					placePanel("Place a road...");
					timer = new Timer(INTERVAL,
							new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if(display.getP().getState() == 3){

							}
							else {
								buyPanel();
								timer.stop();
							}
						}
					});
					timer.start();
				}
				else if (bought == 1) {
					errorPanel("Insufficient resources!");
				}
				else if (bought == 2) {
					errorPanel("Structure capacity reached!");
				}
			}
		});
		buyRoad.setText("road");
		buyPanel.add(new KComponent(buyRoad, new Rectangle(1,8,6,2)));

		// Buy devcard
		JButton buyCard = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu g = display.getP().getGame();

				int bought = g.buyDevCard(GameRunner.getCurrentPlayer());

				if (bought == 0) {
					CarteD dC = g.getDeck().draw();
					GameRunner.getCurrentPlayer().addDevCard(dC);

					buyPanel();
				}
				else if (bought == 1) {
					errorPanel("Insufficient resources!");
				}
				else if (bought == 2) {
					errorPanel("No more dev cards in deck!");
				}
			}
		});
		buyCard.setText("dev card");
		buyPanel.add(new KComponent(buyCard, new Rectangle(7,8,6,2)));

		// Return to main panel
		buyPanel.add(new KComponent(returnMain, new Rectangle(3,12,8,2)));

		// Error panel:
		//-------------------------------------------------------------------

		JLabel message = new JLabel("");
		message.setFont(font);
		errorPanel.add(message, new Rectangle(2,4,10,1));

		JButton accept = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				switch(flag) {
				case 0: setPanel(mainPanel); break;
				case 1: setPanel(tradePanel); break;
				case 2: setPanel(buyPanel); break;
				case 3: setPanel(devPanel); break;
				default: setPanel(mainPanel); break;
				}
			}
		});
		accept.setText("continue");
		errorPanel.add(accept, new Rectangle (3,7,9,2));

		// Dev card panel:
		//-------------------------------------------------------------------

		JLabel devCard = new JLabel("Play a...");
		message.setFont(font);
		devPanel.add(devCard, new Rectangle(4,4,6,2));

		// Play a Chevalier card
		JButton Chevalier = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu g = display.getP().getGame();

				if (GameRunner.getCurrentPlayer().hasCard("Chevalier")) {
					GameRunner.getCurrentPlayer().removeCard("Chevalier");
					GameRunner.getCurrentPlayer().incrementNumbChevaliers();

					display.getP().placeRobber();
					placePanel("Move the robber...");
					timer = new Timer(INTERVAL,
							new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if(display.getP().getState() == 1) {
							}
							else {
								timer.stop();
								GameRunner.getCurrentPlayer().incrementNumbChevaliers();
								//Choose player to steal from (JComboBox)

								stealPanel();
							}
						}
					});
					timer.start();
				}
				else {
					errorPanel("You don't own this card!");
				}
			}
		});
		Chevalier.setText("Chevalier");
		devPanel.add(Chevalier, new Rectangle (1,6,6,2));

		// Play a Amoi card
		JButton Amoi = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				if (GameRunner.getCurrentPlayer().hasCard("Amoi")) {
					GameRunner.getCurrentPlayer().removeCard("Amoi");

					resPanel();
					timer = new Timer(INTERVAL,
							new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if(done){
								timer.stop();
								done = false;

								Jeu g = display.getP().getGame();
								g.takeAll(resSelection, GameRunner.getCurrentPlayer());

								mainPanel();
							}
						}
					});
					timer.start();
					
				}
				else {
					errorPanel("You don't own this card!");
				}
			}
		});
		Amoi.setText("Amoi");
		devPanel.add(new KComponent(Amoi, new Rectangle(7,6,6,2)));

		// Play a AnneeAbondonce card
		JButton year = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				if (GameRunner.getCurrentPlayer().hasCard("AnneeAbondonce")) {
					GameRunner.getCurrentPlayer().removeCard("AnneeAbondonce");
					inputResourcesPanel(2, GameRunner.getCurrentPlayer(), "Choose 2 resources", true);
					timer = new Timer(INTERVAL,
							new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if(IRPdone){
								timer.stop();
								GameRunner.getCurrentPlayer().addResources(inputResources);
								mainPanel();
							}
						}
					});
					timer.start();
				}
				else {
					errorPanel("You don't own this card!");
				}
			}
		});
		year.setText("year 'o plenty");
		devPanel.add(new KComponent(year, new Rectangle(1,8,6,2)));

		// Play a ConstructeurChemin card
		JButton road = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu g = display.getP().getGame();
				if (GameRunner.getCurrentPlayer().hasCard("ConstructeurChemin")) {
					GameRunner.getCurrentPlayer().removeCard("ConstructeurChemin");

					display.getP().placeRoad(2);
					placePanel("Place 2 roads...");

					timer = new Timer(INTERVAL,
							new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if(display.getP().getState() == 3){
							}
							else {
								mainPanel();
								timer.stop();
							}
						}
					});
					timer.start();
				}
				else {
					errorPanel("You don't own this card!");
				}
			}
		});
		road.setText("ConstructeurChemin");
		devPanel.add(new KComponent(road, new Rectangle(7,8,6,2)));

		// Return to main panel
		devPanel.add(new KComponent(returnMain, new Rectangle(3,12,8,2)));

		// Steal panel:
		//-------------------------------------------------------------------

		JComboBox<Joeur> playerStealBox = new JComboBox<Joeur>();
		playerStealBox.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				JComboBox<Joeur> cb = (JComboBox)a.getSource();
				Joeur playerSteal = (Joeur)cb.getSelectedItem();
				display.getP().getGame().takeCard(GameRunner.getCurrentPlayer(), playerSteal);
				mainPanel();
			}
		});

		stealPanel.add(playerStealBox, new Rectangle(3,6,8,2));

		// Choose Trade Partner Panel:
		//-------------------------------------------------------------------

		JComboBox<Joeur> playerTradeBox = new JComboBox<Joeur>();
		playerTradeBox.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				//System.out.println("action!");
				JComboBox<Joeur> cb = (JComboBox)a.getSource();
				tradeChoice = (Joeur)cb.getSelectedItem();
				//System.out.println(tradeChoice);
				inputResourcesPanel(-1, GameRunner.getCurrentPlayer(), "Offer resources", false);
				timer = new Timer(INTERVAL,
						new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						if(IRPdone){
							timer.stop();
							offerResources = inputResources;
							inputResourcesPanel(-1, tradeChoice, "Sell resources", false);
							timer = new Timer(INTERVAL,
									new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									if(IRPdone){
										timer.stop();
										sellResources = inputResources;
										display.getP().getGame().playerTrade(GameRunner.getCurrentPlayer(), tradeChoice, offerResources, sellResources);
										mainPanel();
									}
								}
							});
							timer.start();
						}
					}
				});
				timer.start();
			}
		});

		choosePlayerPanel.add(playerTradeBox, new Rectangle(3,6,8,2));

		// Resource selection panel:
		//-------------------------------------------------------------------

		JButton LAINE = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				resSelection = "LAINE";
				done = true;
			}
		});
		LAINE.setText("LAINE");
		resPanel.add(LAINE, new Rectangle(4,6,6,2));

		JButton BLE = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				resSelection = "BLE";
				done = true;
			}
		});
		BLE.setText("BLE");
		resPanel.add(BLE, new Rectangle(4,8,6,2));

		JButton BOIS = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				resSelection = "BOIS";
				done = true;
			}
		});
		BOIS.setText("BOIS");
		resPanel.add(BOIS, new Rectangle(4,10,6,2));

		JButton ore = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				resSelection = "ORE";
				done = true;
			}
		});
		ore.setText("ore");
		resPanel.add(ore, new Rectangle(4,12,6,2));

		JButton ARGILE = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				resSelection = "ARGILE";
				done = true;
			}
		});
		ARGILE.setText("ARGILE");
		resPanel.add(ARGILE, new Rectangle(4,14,6,2));

		// Place panel
		//-------------------------------------------------------------------

		JLabel mess = new JLabel();
		mess.setFont(font);
		placePanel.add(mess, new Rectangle(2,8,10,4));

		// Setup panel
		//-------------------------------------------------------------------

		final JLabel start = new JLabel("Your setup, " + GameRunner.getCurrentPlayer());
		start.setFont(font);
		setupPanel.add(new KComponent(start, new Rectangle(2,3,10,2)));

		JButton begin = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				//System.out.println(count);
				if (!secondRound) {

					if (count == GameRunner.getNumbPlayers() - 1) {

						secondRound = true;
						count++;
						//Place capitol commandblock
						display.getP().placeSettlementNoRoad(1);
						placePanel("Place first settlement..");

						timer = new Timer(INTERVAL,
								new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if(display.getP().getState() == 5){

								}
								else {
									timer.stop();
									//Place Road commandblock
									display.getP().placeRoad(1);
									placePanel("Place a road...");
									timer = new Timer(INTERVAL,
											new ActionListener() {
										public void actionPerformed(ActionEvent evt) {
											if(display.getP().getState() == 3){

											}
											else {
												timer.stop();
												setCurrentPlayer(GameRunner.getCurrentPlayer());
												start.setText("Place a settlement, " + GameRunner.getCurrentPlayer());
												setupPanel();
											}
										}
									});
									timer.start();
									//
								}
							}
						});
						timer.start();
						//
					}
					else {
						count++;
						//Place SettlementNoRoad commandblock
						display.getP().placeSettlementNoRoad(1);
						placePanel("Place first settlement...");
						timer = new Timer(INTERVAL,
								new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if(display.getP().getState() == 5){

								}
								else {
									timer.stop();
									//Place Road commandblock
									display.getP().placeRoad(1);
									placePanel("Place a road...");
									timer = new Timer(INTERVAL,
											new ActionListener() {
										public void actionPerformed(ActionEvent evt) {
											if(display.getP().getState() == 3){

											}
											else {
												timer.stop();
												GameRunner.nextPlayer();
												setCurrentPlayer(GameRunner.getCurrentPlayer());
												start.setText("Place a settlement, " + GameRunner.getCurrentPlayer());
												setupPanel();
											}
										}
									});
									timer.start();
									//
								}
							}
						});
						timer.start();
						//
					}
				}
				else {

					if (count == 1) {
						//Place capitol commandblock
						display.getP().placeCapitol();
						placePanel("Place your capitol...");
						timer = new Timer(INTERVAL,
								new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if(display.getP().getState() == 5){

								}
								else {
									timer.stop();
									//Place Road commandblock
									display.getP().placeRoad(1);
									placePanel("Place a road...");
									timer = new Timer(INTERVAL,
											new ActionListener() {
										public void actionPerformed(ActionEvent evt) {
											if(display.getP().getState() == 3){

											}
											else {
												timer.stop();
												//Collections.shuffle(GameRunner.players);
												setCurrentPlayer(GameRunner.getCurrentPlayer());
												rollPanel();
											}
										}
									});
									timer.start();
									//
								}
							}
						});
						timer.start();
						//
					}
					else {
						count--;
						//Place capitol commandblock
						display.getP().placeCapitol();
						placePanel("Place your capitol...");
						timer = new Timer(INTERVAL,
								new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if(display.getP().getState() == 5){

								}
								else {
									timer.stop();
									//Place Road commandblock
									display.getP().placeRoad(1);
									placePanel("Place a road...");
									timer = new Timer(INTERVAL,
											new ActionListener() {
										public void actionPerformed(ActionEvent evt) {
											if(display.getP().getState() == 3){

											}
											else {
												timer.stop();
												GameRunner.prevPlayer();
												setCurrentPlayer(GameRunner.getCurrentPlayer());
												start.setText("Place your capitol, " + GameRunner.getCurrentPlayer());
												setupPanel();
											}
										}
									});
									timer.start();
									//
								}
							}
						});
						timer.start();
						//
					}
				}
			}
		});
		begin.setText("place");
		setupPanel.add(new KComponent(begin, new Rectangle(4,6,6,3)));

		// Input Resources Panel
		//-------------------------------------------------------------------

		JComboBox<Integer> ARGILECombo = new JComboBox<Integer>();
		ARGILECombo.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
			}
		});

		inputResourcesPanel.add(ARGILECombo, new Rectangle(2,6,3,1));

		JComboBox<Integer> LAINECombo = new JComboBox<Integer>();
		LAINECombo.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
			}
		});

		inputResourcesPanel.add(LAINECombo, new Rectangle(6,6,3,1));

		JComboBox<Integer> oreCombo = new JComboBox<Integer>();
		oreCombo.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
			}
		});

		inputResourcesPanel.add(oreCombo, new Rectangle(10,6,3,1));

		JComboBox<Integer> BLECombo = new JComboBox<Integer>();
		BLECombo.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
			}
		});

		inputResourcesPanel.add(BLECombo, new Rectangle(4,10,3,1));

		JComboBox<Integer> BOISCombo = new JComboBox<Integer>();
		BOISCombo.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
			}
		});

		inputResourcesPanel.add(BOISCombo, new Rectangle(8,10,3,1));

		JLabel playerLabel= new JLabel("Player: ");
		start.setFont(font);
		inputResourcesPanel.add(new KComponent(playerLabel, new Rectangle(2,3,15,1)));

		JButton submitResources = new JButton();
		submitResources.setText("Submit");
		inputResourcesPanel.add(submitResources, new Rectangle(3,15,9,2));

		JLabel ARGILELabel = new JLabel("ARGILE");
		start.setFont(font);
		inputResourcesPanel.add(new KComponent(ARGILELabel, new Rectangle(2,5,4,1)));

		JLabel LAINELabel = new JLabel("LAINE");
		start.setFont(font);
		inputResourcesPanel.add(new KComponent(LAINELabel, new Rectangle(6,5,4,1)));

		JLabel oreLabel = new JLabel("Ore");
		start.setFont(font);
		inputResourcesPanel.add(new KComponent(oreLabel, new Rectangle(10,5,4,1)));

		JLabel BLELabel = new JLabel("BLE");
		start.setFont(font);
		inputResourcesPanel.add(new KComponent(BLELabel, new Rectangle(4,9,4,1)));

		JLabel BOISLabel = new JLabel("BOIS");
		start.setFont(font);
		inputResourcesPanel.add(new KComponent(BOISLabel, new Rectangle(8,9,4,1)));
		
		//inputResourcesPanel(1, GameRunner.getCurrentPlayer(), "test");

		//-------------------------------------------------------------------

		setupPanel();
//		mainPanel();
//		if (!GameRunner.getCurrentPlayer().getName().equals("DevMaster"))
//			GameRunner.nextPlayer();
//		if (!GameRunner.getCurrentPlayer().getName().equals("DevMaster"))
//			GameRunner.nextPlayer();
//		if (!GameRunner.getCurrentPlayer().getName().equals("DevMaster"))
//			GameRunner.nextPlayer();
//		devPanel();
//		rollPanel();
	}

	private void setPanel(ListC cL) {
		this.removeAll();
		this.add(currentPlayerBox.getComponent(), currentPlayerBox.getRectangle());

		for (int i = 0; i < cL.size(); i++) {
			this.add(cL.get(i).getComponent(), cL.get(i).getRectangle());
		}

		repaint();
		validate();
	}

	public void buyPanel() {
		setPanel(buyPanel);
		flag = 2;
	}

	public void tradePanel() {
		setPanel(tradePanel);
		flag = 1;
	}

	public void rollPanel() {
		setCurrentPlayer(GameRunner.getCurrentPlayer());
		setPanel(rollPanel);
		flag = 0;
	}

	public void mainPanel() {
		setPanel(mainPanel);
		flag = 0;
	}

	public void errorPanel(String str) {
		((JLabel) errorPanel.get(0).getComponent()).setText(str);
		setPanel(errorPanel);
	}

	public void devPanel() {
		setPanel(devPanel);
		flag = 3;
	}

	public void resPanel() {
		setPanel(resPanel);
	}

	public void stealPanel() {
		//JComboBox<Player> newBox = new JComboBox<Player>();
		AbstractAction action = (AbstractAction) ((JComboBox<Joeur>) stealPanel.get(0).getComponent()).getAction();
		((JComboBox<Joeur>) stealPanel.get(0).getComponent()).setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				
			}
		});
		((JComboBox<Joeur>) stealPanel.get(0).getComponent()).removeAllItems();
		for (int i = 0; i < display.getP().getGame().getP().getRobberAdjacentPlayers().size(); i++) {
			if (display.getP().getGame().getP().getRobberAdjacentPlayers().get(i).equals(GameRunner.getCurrentPlayer())) {
			}
			else {
				((JComboBox<Joeur>) stealPanel.get(0).getComponent()).addItem(display.getP().getGame().getP().getRobberAdjacentPlayers().get(i));
			}
		}

		((JComboBox<Joeur>) stealPanel.get(0).getComponent()).setAction(action);
		//System.out.println(((JComboBox<Player>) stealPanel.get(0).getComponent()).getItemCount());
		//System.out.println(((JComboBox<Player>) stealPanel.get(0).getComponent()).getItemAt(0));

		if (((JComboBox<Joeur>) stealPanel.get(0).getComponent()).getItemCount() <= 0) {
			//System.out.println("IF");
			errorPanel("No one to steal from");
		}
		else {
			//System.out.println("ELSE");
			setPanel(stealPanel);
		}
	}
	
	public void choosePlayerPanel() {
		AbstractAction action = (AbstractAction) ((JComboBox<Joeur>) choosePlayerPanel.get(0).getComponent()).getAction();
		// Remove action, so that removeAllItems() does not trigger an event
		((JComboBox<Joeur>) choosePlayerPanel.get(0).getComponent()).setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				
			}
		});
		// Depopulate combo box
		((JComboBox<Joeur>) choosePlayerPanel.get(0).getComponent()).removeAllItems();
		// Populate combo box
		for (int i = 0; i < GameRunner.getNumbPlayers(); i++) {
			if (GameRunner.getPlayer(i).equals(GameRunner.getCurrentPlayer())) {
			}
			else {
				((JComboBox<Joeur>) choosePlayerPanel.get(0).getComponent()).addItem(GameRunner.getPlayer(i));
			}
		}
		// Reset action
		((JComboBox<Joeur>) choosePlayerPanel.get(0).getComponent()).setAction(action);
		setPanel(choosePlayerPanel);
	}

	public void placePanel(String str) {
		((JLabel) placePanel.get(0).getComponent()).setText(str);
		setPanel(placePanel);
	}

	/**
	 * 
	 * 
	 * @param n Number of resources to be selected, -1 for any
	 * @param p the player inputting resources
	 * @param str to display on submit button
	 * @return ArrayList<String> of resources selected
	 */
	public void inputResourcesPanel(final int n, final Joeur p, String str, final boolean YOP) {
		//final ArrayList<String> output = new ArrayList<String>();
		IRPdone = false;

		// Depopulates / Repopulates the combo boxes
		for (int i = 0; i < 5; i++) {
			((JComboBox<Integer>) inputResourcesPanel.get(i).getComponent()).removeAllItems();
		}
		if (YOP) {
			for (int r = 0; r <= 2; r++) {
				((JComboBox<Integer>) inputResourcesPanel.get(0).getComponent()).addItem(new Integer(r));
			}
			for (int r = 0; r <= 2; r++) {
				((JComboBox<Integer>) inputResourcesPanel.get(1).getComponent()).addItem(new Integer(r));
			}
			for (int r = 0; r <= 2; r++) {
				((JComboBox<Integer>) inputResourcesPanel.get(2).getComponent()).addItem(new Integer(r));
			}
			for (int r = 0; r <= 2; r++) {
				((JComboBox<Integer>) inputResourcesPanel.get(3).getComponent()).addItem(new Integer(r));
			}
			for (int r = 0; r <= 2; r++) {
				((JComboBox<Integer>) inputResourcesPanel.get(4).getComponent()).addItem(new Integer(r));
			}
		}
		else {
			for (int r = 0; r <= p.getNumberResourcesType("ARGILE"); r++) {
				((JComboBox<Integer>) inputResourcesPanel.get(0).getComponent()).addItem(new Integer(r));
			}
			for (int r = 0; r <= p.getNumberResourcesType("LAINE"); r++) {
				((JComboBox<Integer>) inputResourcesPanel.get(1).getComponent()).addItem(new Integer(r));
			}
			for (int r = 0; r <= p.getNumberResourcesType("ORE"); r++) {
				((JComboBox<Integer>) inputResourcesPanel.get(2).getComponent()).addItem(new Integer(r));
			}
			for (int r = 0; r <= p.getNumberResourcesType("BLE"); r++) {
				((JComboBox<Integer>) inputResourcesPanel.get(3).getComponent()).addItem(new Integer(r));
			}
			for (int r = 0; r <= p.getNumberResourcesType("BOIS"); r++) {
				((JComboBox<Integer>) inputResourcesPanel.get(4).getComponent()).addItem(new Integer(r));
			}
		}

		//Sets player
		((JLabel) inputResourcesPanel.get(5).getComponent()).setText("Player: " + p.getName());

		// Sets action according to flag and resourceCount
		((JButton) inputResourcesPanel.get(6).getComponent()).setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				int sum = 0;
				for (int i = 0; i < 5; i++) {
					sum += ((JComboBox<Integer>) inputResourcesPanel.get(i).getComponent()).getSelectedIndex();
				}
				//System.out.println(sum);
				if (n != -1) {
					if (sum != n) {
						return;
					}
				}
				ArrayList<String> output2 = new ArrayList<String>();

				for (int i = 0; i < ((JComboBox<Integer>) inputResourcesPanel.get(0).getComponent()).getSelectedIndex(); i++) {
					output2.add("ARGILE");
				}
				for (int i = 0; i < ((JComboBox<Integer>) inputResourcesPanel.get(1).getComponent()).getSelectedIndex(); i++) {
					output2.add("LAINE");
				}
				for (int i = 0; i < ((JComboBox<Integer>) inputResourcesPanel.get(2).getComponent()).getSelectedIndex(); i++) {
					output2.add("ORE");
				}
				for (int i = 0; i < ((JComboBox<Integer>) inputResourcesPanel.get(3).getComponent()).getSelectedIndex(); i++) {
					output2.add("BLE");
				}
				for (int i = 0; i < ((JComboBox<Integer>) inputResourcesPanel.get(4).getComponent()).getSelectedIndex(); i++) {
					output2.add("BOIS");
				}

				inputResources = output2;
				//System.out.println("Arrived");
				IRPdone = true;
				/*
				switch (flag) {
				case 0:
					System.out.println(inputResources);
					System.out.println("blargh");
					p.removeResources(inputResources);
					break;
				case 1:
					break;
				}
				 */

			}
		});

		// Sets submit button text
		((JButton) inputResourcesPanel.get(6).getComponent()).setText(str);

		setPanel(inputResourcesPanel);
		/*
		timer = new Timer(INTERVAL,
				new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						if(continue){
							return output;
						}
					}
				});
		timer.start();
		 */

	}

	public void setCurrentPlayer(Joeur p) {
		JLabel label = (JLabel) currentPlayerBox.getComponent();
		label.setText("Player: " + p.getName());
		label.setOpaque(true);
		label.setBackground(GameRunner.getCurrentPlayer().getColor());
	}

	public void setupPanel() {
		setPanel(setupPanel);
	}

	public void winPanel() {
		this.removeAll();

		JLabel win = new JLabel(GameRunner.getWinner().getName() + " wins!");
		win.setFont(new Font("Arial", 1, 24));
		this.add(win, new Rectangle(2,4,10,5));
	}
}
